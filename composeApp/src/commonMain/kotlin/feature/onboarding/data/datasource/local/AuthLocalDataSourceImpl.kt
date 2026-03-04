package feature.onboarding.data.datasource.local

import com.russhwolf.settings.Settings
import feature.onboarding.data.datasource.AuthDataSource
import feature.onboarding.data.model.entity.UserEntity
import kotlinx.serialization.json.Json

class AuthLocalDataSourceImpl(
    private val secureSettings: Settings
) : AuthDataSource.Local {
    
    companion object {
        const val KEY_USER_DATA = "user_data"
        const val KEY_USER_TOKEN = "user_token"
        const val KEY_USER_REFRESH_TOKEN = "user_refresh_token"
    }

    override suspend fun saveUser(userEntity: UserEntity) {
        val json = Json.encodeToString(userEntity)
        secureSettings.putString(KEY_USER_DATA, json)
        secureSettings.putString(KEY_USER_TOKEN, userEntity.token)
    }

    override suspend fun getUser(): UserEntity? {
        val json = secureSettings.getStringOrNull(KEY_USER_DATA) ?: return null
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            clearUser()
            null
        }
    }

    override suspend fun clearUser() {
        secureSettings.remove(KEY_USER_DATA)
        secureSettings.remove(KEY_USER_TOKEN)
        secureSettings.remove(KEY_USER_REFRESH_TOKEN)
    }

    override suspend fun getToken(): String? {
        return secureSettings.getStringOrNull(KEY_USER_TOKEN)
    }

    override suspend fun saveToken(token: String) {
        secureSettings.putString(KEY_USER_TOKEN, token)
    }

    override suspend fun clearToken() {
        secureSettings.remove(KEY_USER_TOKEN)
    }

    override suspend fun getRefreshToken(): String? {
        return secureSettings.getStringOrNull(KEY_USER_REFRESH_TOKEN)
    }

    override suspend fun saveRefreshToken(token: String) {
        secureSettings.putString(KEY_USER_REFRESH_TOKEN, token)
    }

    override suspend fun clearRefreshToken() {
        secureSettings.remove(KEY_USER_REFRESH_TOKEN)
    }
}
