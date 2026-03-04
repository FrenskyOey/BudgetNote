package feature.onboarding.data.repository

import core.domain.model.Result
import feature.onboarding.data.datasource.AuthDataSource
import feature.onboarding.data.model.mapper.toDomain
import feature.onboarding.data.model.mapper.toEntity
import feature.onboarding.data.model.request.LoginRequest
import feature.onboarding.domain.model.LoginCredentials
import feature.onboarding.domain.model.User
import feature.onboarding.domain.repository.AuthRepository
import core.domain.model.AppException
import core.data.remote.util.ApiErrorHandler
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class AuthRepositoryImpl(
    private val remoteDataSource: AuthDataSource.Remote,
    private val localDataSource: AuthDataSource.Local,
) : AuthRepository {

    private val refreshMutex = Mutex()

    override suspend fun login(credentials: LoginCredentials): Result<User> {
        return try {
            val request = LoginRequest(credentials.email, credentials.password)
            val response = remoteDataSource.login(request)
            
            if (response.isSuccess && response.data != null) {
                val user = response.data.toDomain()
                localDataSource.saveUser(user.toEntity())
                localDataSource.saveToken(response.data.token)
                localDataSource.saveRefreshToken(response.data.refreshToken)
                Result.Success(user)
            } else {
                // If API returns false success, it might still have an error message
                // For now, we wrap it in AuthException
                Result.Error(AppException.AuthException(response.errorMessage ?: "Unknown login error"))
            }
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            localDataSource.clearUser()
            localDataSource.clearToken()
            localDataSource.clearRefreshToken()
            Result.Success(Unit)
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            val userEntity = localDataSource.getUser()
            Result.Success(userEntity?.toDomain())
        } catch (e: Exception) {
            handleError(e)
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return localDataSource.getToken() != null
    }

    override suspend fun refreshToken(failedAccessToken: String?): Result<String> {
        return refreshMutex.withLock {
            try {
                val currentToken = localDataSource.getToken()
                // Double-Checked Locking: If the current token is already different from the one that failed,
                // it means another thread in the queue already refreshed it. Just return the new token!
                if (failedAccessToken != null && currentToken != null && currentToken != failedAccessToken) {
                    return Result.Success(currentToken)
                }

                val currentRefreshToken = localDataSource.getRefreshToken()
                    ?: return Result.Error(AppException.AuthException("No refresh token stored"))

                val response = remoteDataSource.refreshToken(currentRefreshToken)
                
                if (response.isSuccess && response.data != null) {
                    localDataSource.saveToken(response.data.token)
                    localDataSource.saveRefreshToken(response.data.refreshToken)
                    Result.Success(response.data.token)
                } else {
                    logout()
                    Result.Error(AppException.AuthException(response.errorMessage ?: "Failed to refresh token"))
                }
            } catch (e: Exception) {
                logout()
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception): Result.Error {
        val appException = ApiErrorHandler.handleError(e)
        return Result.Error(appException)
    }
}
