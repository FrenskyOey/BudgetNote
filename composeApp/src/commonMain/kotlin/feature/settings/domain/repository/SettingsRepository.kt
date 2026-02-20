package feature.settings.domain.repository

import core.domain.model.Result
import feature.settings.domain.model.AppSettings

interface SettingsRepository {
    suspend fun getAppSettings(): Result<AppSettings>
}
