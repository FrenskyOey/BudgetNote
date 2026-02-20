package core.domain.config

interface AppConfig {
    val baseApiUrl: String
    val flavorName: String
    val supabaseUrl: String
    val supabaseAnonKey: String
}

expect fun createAppConfig(): AppConfig
