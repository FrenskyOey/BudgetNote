package feature.onboarding.data.api

import feature.onboarding.data.model.request.LoginRequest
import feature.onboarding.data.model.response.LoginResponse

interface AuthApiService {
    suspend fun login(request: LoginRequest): LoginResponse
}
