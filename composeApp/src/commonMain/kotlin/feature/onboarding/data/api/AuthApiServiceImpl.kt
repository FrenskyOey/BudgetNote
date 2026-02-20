package feature.onboarding.data.api

import core.domain.config.AppConfig
import feature.onboarding.data.model.request.LoginRequest
import feature.onboarding.data.model.response.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApiServiceImpl(
    private val httpClient: HttpClient,
    private val appConfig: AppConfig
) : AuthApiService {
    override suspend fun login(request: LoginRequest): LoginResponse {
        val endpoint = "${appConfig.baseApiUrl}login"
        return httpClient.post(endpoint) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(mapOf(
                "userName" to request.userName,
                "password" to request.password
            ).entries.joinToString("&") { "${it.key}=${it.value}" })
        }.body()
    }
}
