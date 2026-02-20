package core.di

import core.domain.config.createAppConfig
import org.koin.dsl.module

val coreConfigModule = module {
    single { createAppConfig() }
}
