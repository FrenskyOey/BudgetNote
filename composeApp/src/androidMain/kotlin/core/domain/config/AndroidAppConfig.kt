package core.domain.config

import com.app.budgetnote.BuildConfig

class AndroidAppConfig : AppConfig {
    override val baseApiUrl: String = BuildConfig.BASE_API_URL
    override val flavorName: String = BuildConfig.FLAVOR_NAME
    override val supabaseUrl: String = BuildConfig.SUPABASE_URL
    override val supabaseAnonKey: String = BuildConfig.SUPABASE_ANON_KEY
}

actual fun createAppConfig(): AppConfig = AndroidAppConfig()
