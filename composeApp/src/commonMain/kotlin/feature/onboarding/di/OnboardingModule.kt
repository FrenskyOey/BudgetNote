package feature.onboarding.di

import feature.onboarding.data.api.AuthApiService
import feature.onboarding.data.api.AuthApiServiceImpl
import feature.onboarding.data.datasource.AuthDataSource
import feature.onboarding.data.datasource.local.AuthLocalDataSourceImpl
import feature.onboarding.data.datasource.remote.AuthRemoteDataSourceImpl
import feature.onboarding.data.repository.AuthRepositoryImpl
import feature.onboarding.domain.repository.AuthRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

import feature.onboarding.ui.viewmodel.LoginViewModel
import org.koin.core.module.dsl.viewModel

val onboardingModule = module {
    // Data Sources
    single<AuthDataSource.Remote> { AuthRemoteDataSourceImpl(get()) }
    single<AuthDataSource.Local> { AuthLocalDataSourceImpl(get(named("secure"))) }
    
    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    
    // UseCases
    // Validators
    single { feature.onboarding.domain.usecase.ValidateEmailUseCase() }
    single { feature.onboarding.domain.usecase.ValidatePasswordUseCase() }
    
    // Domain Logic
    single { feature.onboarding.domain.usecase.LoginUseCase(get(), get(), get()) }
    single { feature.onboarding.domain.usecase.GoogleSignInUseCase(get()) }
    
    // ViewModel
    viewModel { LoginViewModel(get(), get(), get(), get(), get()) }
}
