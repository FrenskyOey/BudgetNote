package core.di

import core.util.log.LogHelper
import core.util.log.LogInterface
import core.util.log.PlatformLogger
import org.koin.dsl.module

/**
 * Koin module for core utilities.
 *
 * - [Log] → [PlatformLogger]  (platform-specific; swap for a fake in tests)
 * - [LogHelper]               (inject wherever logging is needed)
 */
val coreUtilModule = module {
    single<LogInterface> { PlatformLogger() }
    single { LogHelper(get()) }
}
