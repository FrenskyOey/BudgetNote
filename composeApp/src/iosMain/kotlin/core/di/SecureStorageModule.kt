package core.di

import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val secureStorageModule: Module = module {
    single<Settings>(named("secure")) {
        KeychainSettings(service = "auth")
    }
}
