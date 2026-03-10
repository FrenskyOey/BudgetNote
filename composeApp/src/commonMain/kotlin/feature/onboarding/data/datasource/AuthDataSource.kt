package feature.onboarding.data.datasource

import feature.onboarding.data.model.request.LoginRequest
import feature.onboarding.data.model.response.LoginResponse
import feature.onboarding.data.model.entity.UserEntity

interface AuthDataSource {
    interface Remote {
        suspend fun login(request: LoginRequest): LoginResponse
        suspend fun refreshToken(refreshToken: String): LoginResponse
        suspend fun loginWithGoogle(idToken: String, accessToken: String): LoginResponse
    }

    interface Local {
        suspend fun saveUser(userEntity: UserEntity)
        suspend fun getUser(): UserEntity?
        suspend fun clearUser()
        suspend fun getToken(): String?
        suspend fun saveToken(token: String)
        suspend fun clearToken()
        suspend fun getRefreshToken(): String?
        suspend fun saveRefreshToken(token: String)
        suspend fun clearRefreshToken()
    }
}
