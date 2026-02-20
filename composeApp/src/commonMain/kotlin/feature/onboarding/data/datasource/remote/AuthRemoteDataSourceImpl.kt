package feature.onboarding.data.datasource.remote

import core.domain.model.AppException
import feature.onboarding.data.api.AuthApiService
import feature.onboarding.data.datasource.AuthDataSource
import feature.onboarding.data.model.request.LoginRequest
import feature.onboarding.data.model.response.LoginResponse

import core.data.remote.util.ApiErrorHandler

class AuthRemoteDataSourceImpl(
    private val apiService: AuthApiService
) : AuthDataSource.Remote {
    override suspend fun login(request: LoginRequest): LoginResponse {
        return try {
            val response = apiService.login(request)
            if (response.isSuccess) {
                response
            } else {
                throw AppException.AuthException(response.errorMessage ?: "Unknown login error")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
