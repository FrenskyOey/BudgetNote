package core.di

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.functions.Functions
import org.koin.dsl.module
import core.domain.config.createAppConfig

val coreSupabaseModule = module {
    single<SupabaseClient> {
        val config = createAppConfig()
        val supabaseKey = config.supabaseAnonKey
        
        createSupabaseClient(
             supabaseUrl = config.supabaseUrl,
             supabaseKey = supabaseKey
         ) {
             install(Auth)
             install(Functions)
         }
    }
}
