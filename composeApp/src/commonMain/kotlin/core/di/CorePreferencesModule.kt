package core.di

import core.data.local.preferences.PreferencesManager
import core.data.local.preferences.PreferencesManagerImpl
import org.koin.dsl.module

val corePreferencesModule = module {
    // Note: DataStore instance needs to be provided by platform specific module or setup
    // For now we assume generic DataStore provision or we'll add it later.
    // Actually, creating DataStore in KMP requires platform specific code (Path).
    // For Phase 1, we will just bind the implementation assuming DataStore is provided.
    
    single<PreferencesManager> { PreferencesManagerImpl(get()) }
}
