package feature.settings.di

import feature.settings.data.repository.SettingsRepositoryImpl
import feature.settings.domain.repository.SettingsRepository
import feature.settings.domain.usecase.GetSettingsUseCase
import org.koin.dsl.module

val settingsModule = module {
    // Repository
    single<SettingsRepository> { 
        SettingsRepositoryImpl()
    }
    
    // Use Cases
    factory { GetSettingsUseCase(get()) }
}
