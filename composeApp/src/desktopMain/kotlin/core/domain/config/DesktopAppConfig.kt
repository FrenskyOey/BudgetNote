package core.domain.config

class DesktopAppConfig : AppConfig {
    override val baseApiUrl: String = "https://cd841015-7e2e-4a18-8082-a7c23d45097e.mock.pstmn.io/v1/"
    override val flavorName: String = "Desktop"
    override val supabaseUrl: String = ""
    override val supabaseAnonKey: String = ""
}

actual fun createAppConfig(): AppConfig = DesktopAppConfig()
