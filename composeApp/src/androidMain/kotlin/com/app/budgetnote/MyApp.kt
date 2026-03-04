package com.app.budgetnote

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import core.di.coreDatabaseModule
import core.di.coreNetworkModule
import core.di.corePreferencesModule
import core.di.coreConfigModule
import core.di.coreSupabaseModule
import core.di.secureStorageModule
import di.appModule
import feature.onboarding.di.onboardingModule
import feature.settings.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

import core.data.local.database.appContext

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this


        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .components {
                    add(KtorNetworkFetcherFactory())
                }
                .build()
        }

        startKoin {
            androidContext(this@MyApp)
            modules(
                // Core modules
                coreNetworkModule,
                coreDatabaseModule,
                corePreferencesModule,
                coreConfigModule,
                secureStorageModule,
                coreSupabaseModule,
                // Feature modules
                settingsModule,
                onboardingModule,
                // App module
                appModule
            )
        }
    }
}
