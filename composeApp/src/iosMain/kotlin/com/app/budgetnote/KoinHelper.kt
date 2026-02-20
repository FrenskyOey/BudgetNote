package com.app.budgetnote

import core.di.coreConfigModule
import core.di.coreDatabaseModule
import core.di.coreNetworkModule
import core.di.corePreferencesModule
import core.di.secureStorageModule
import di.appModule
import feature.onboarding.di.onboardingModule

import feature.settings.di.settingsModule
import org.koin.core.context.startKoin

fun doInitKoin() {
    startKoin {
        modules(
            coreNetworkModule,
            coreDatabaseModule,
            corePreferencesModule,
            coreConfigModule,
            secureStorageModule,
            settingsModule,
            onboardingModule,
            appModule
        )
    }
}
