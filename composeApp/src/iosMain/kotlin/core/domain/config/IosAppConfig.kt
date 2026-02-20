package core.domain.config

import platform.Foundation.NSBundle

class IosAppConfig : AppConfig {
    // In a real app, you might read these from Info.plist
    // val url = NSBundle.mainBundle.objectForInfoDictionaryKey("BASE_API_URL") as? String
    
    // For now, adhering to user request to "store" it on iOS side.
    // We will mimic the dev environment values here.
    
    override val baseApiUrl: String = "https://cd841015-7e2e-4a18-8082-a7c23d45097e.mock.pstmn.io/v1/"
    override val flavorName: String = "dev"
    override val supabaseUrl: String = ""
    override val supabaseAnonKey: String = ""
}

actual fun createAppConfig(): AppConfig = IosAppConfig()
