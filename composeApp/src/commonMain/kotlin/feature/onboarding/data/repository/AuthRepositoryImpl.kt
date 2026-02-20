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

class AuthRepositoryImpl(
    private val remoteDataSource: AuthDataSource.Remote,
    private val localDataSource: AuthDataSource.Local,
) : AuthRepository {
    override suspend fun login(credentials: LoginCredentials): Result<User> {
        return try {
            // credentials.password is already hashed by LoginUseCase
            val request = LoginRequest(credentials.email, credentials.password)
            val response = remoteDataSource.login(request)
            
            if (response.isSuccess && response.data != null) {
                val user = response.data.toDomain()
                localDataSource.saveUser(user.toEntity())
                localDataSource.saveToken(response.data.token)
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

    private fun handleError(e: Exception): Result.Error {
        val appException = ApiErrorHandler.handleError(e)
        return Result.Error(appException)
    }
}
