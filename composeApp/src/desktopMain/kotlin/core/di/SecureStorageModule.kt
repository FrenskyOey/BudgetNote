package core.di

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.prefs.Preferences

actual val secureStorageModule: Module = module {
    single<Settings>(named("secure")) {
        PreferencesSettings(Preferences.userRoot().node("auth_prefs"))
    }
}
