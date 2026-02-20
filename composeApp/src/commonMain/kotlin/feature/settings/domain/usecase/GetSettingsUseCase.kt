package feature.settings.domain.usecase

import core.domain.model.Result
import feature.settings.domain.model.AppSettings
import feature.settings.domain.repository.SettingsRepository

class GetSettingsUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(): Result<AppSettings> {
        return repository.getAppSettings()
    }
}
