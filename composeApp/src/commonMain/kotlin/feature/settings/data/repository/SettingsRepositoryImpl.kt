package feature.settings.data.repository

import core.domain.model.Result
import feature.settings.domain.model.AppSettings
import feature.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl : SettingsRepository {
    override suspend fun getAppSettings(): Result<AppSettings> {
        return Result.Success(AppSettings("Default"))
    }
}
