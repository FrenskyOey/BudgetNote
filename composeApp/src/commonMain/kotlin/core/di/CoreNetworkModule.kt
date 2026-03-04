package core.di

import core.data.remote.util.JsonSerializer
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import org.koin.dsl.module

import com.russhwolf.settings.Settings
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import core.domain.provider.DeviceMetadataProvider
import core.domain.provider.createDeviceMetadataProvider
import org.koin.core.qualifier.named

import core.data.repository.SessionRepositoryImpl
import core.domain.model.Result
import core.domain.repository.SessionRepository
import feature.onboarding.domain.repository.AuthRepository
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.http.HttpStatusCode

val coreNetworkModule = module {
    single<SessionRepository> { SessionRepositoryImpl(get(named("secure"))) }
    single<DeviceMetadataProvider> { createDeviceMetadataProvider() }

    single {
        val secureSettings: Settings = get(named("secure"))
        val sessionRepository: SessionRepository = get()
        val authRepository: AuthRepository = get()
        val deviceMetadataProvider: DeviceMetadataProvider = get()
        
        HttpClient {
            defaultRequest {
                val deviceInfo = deviceMetadataProvider.getDeviceInfo()
                header("X-Device-OS", deviceInfo.osName)
                header("X-Device-OS-Version", deviceInfo.osVersion)
                header("X-Device-Model", deviceInfo.deviceModel)
                header("X-App-Version", deviceInfo.appVersion)
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val token = secureSettings.getStringOrNull("user_token")
                        if (token != null) {
                            BearerTokens(token, "")
                        } else {
                            null
                        }
                    }
                    
                    refreshTokens {
                        val failedToken = oldTokens?.accessToken
                        val result = authRepository.refreshToken(failedToken)
                        if (result is Result.Success) {
                            BearerTokens(result.data, "")
                        } else {
                            null
                        }
                    }
                }
            }
            install(ContentNegotiation) {
                json(JsonSerializer.json)
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            
            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status == HttpStatusCode.Unauthorized) {
                        sessionRepository.invalidateSession()
                    }
                }
            }
        }
    }
}
