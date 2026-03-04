package core.domain.config

import com.app.budgetnote.core.config.BuildKonfig

interface AppConfig {
    val baseApiUrl: String
    val flavorName: String
    val supabaseAnonKey: String
    val supabaseUrl: String
    val platform: String
}

class AppConfigImpl : AppConfig {
    override val baseApiUrl: String = BuildKonfig.BASE_API_URL
    override val flavorName: String = BuildKonfig.FLAVOR_NAME
    override val supabaseAnonKey: String = BuildKonfig.SUPABASE_KEY
    override val supabaseUrl: String = BuildKonfig.SUPABASE_URL
    override val platform: String = BuildKonfig.PLATFORM
}

fun createAppConfig(): AppConfig = AppConfigImpl()
