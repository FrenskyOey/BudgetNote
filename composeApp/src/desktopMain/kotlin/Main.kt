import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.app.budgetnote.App
import core.di.coreDatabaseModule
import core.di.coreNetworkModule
import core.di.corePreferencesModule
import core.di.coreConfigModule
import core.di.coreSupabaseModule
import core.di.coreUtilModule
import core.di.secureStorageModule
import di.appModule
import feature.onboarding.di.onboardingModule
import feature.settings.di.settingsModule
import org.koin.core.context.startKoin

fun main() = application {
    // Initialize Koin for desktop
    startKoin {
        modules(
            // Core modules
            coreNetworkModule,
            coreDatabaseModule,
            corePreferencesModule,
            coreConfigModule,
            secureStorageModule,
            coreSupabaseModule,
            coreUtilModule,
            
            // Feature modules
            settingsModule,
            onboardingModule,
            
            // App module
            appModule
        )
    }
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "Budget Notes Apps",
        state = rememberWindowState(width = 400.dp, height = 800.dp),
        alwaysOnTop = true,
    ) {
        App()
    }
}
