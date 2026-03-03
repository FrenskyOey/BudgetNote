---
description: Dependency injection setup and rules using Koin
---

# Dependency Injection Rules

## Feature Modules

In KMP, feature modules are defined in `commonMain` and can use `single`, `factory`, and `viewModel` for conciseness.

```kotlin
// feature/onboarding/di/OnboardingModule.kt

val onboardingModule = module {
    // API Service
    single<AuthApiService> { AuthApiServiceImpl(get(), get()) }

    // Data Sources
    single<AuthDataSource.Remote> { AuthRemoteDataSourceImpl(get()) }
    single<AuthDataSource.Local> { AuthLocalDataSourceImpl(get(named("secure"))) }

    // Repository
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    // Use Cases (validators as singletons, domain logic as singletons)
    single { ValidateEmailUseCase() }
    single { ValidatePasswordUseCase() }
    single { LoginUseCase(get(), get(), get()) }

    // ViewModel
    viewModel { LoginViewModel(get(), get(), get(), get()) }
}
```

> [!NOTE]
> - Use `single` for services, repositories, data sources, and use cases.
> - Use `factory` for use cases that need a fresh instance per injection.
> - Use `viewModel` (from `org.koin.core.module.dsl`) for all ViewModels.
> - Use `named("secure")` qualifier when injecting the secure `Settings` instance.

---

## Core Modules

Core modules are always registered before feature modules:

| Module | Responsibility |
|---|---|
| `coreNetworkModule` | `HttpClient` (Ktor + Bearer auth), `SessionRepository` |
| `coreDatabaseModule` | `AppDatabase`, DAOs (e.g., `sampleDao()`) |
| `corePreferencesModule` | Plain `Settings` (non-secure preferences) |
| `coreConfigModule` | `AppConfig` (base URLs, environment config) |
| `secureStorageModule` | Encrypted `Settings` qualified as `named("secure")` — platform-specific (`expect`) |

---

## Module Organization & Initialization

### Shared Module List

All modules are registered in **Android**, **Desktop**, and **iOS** entry points in this order:

```
Core:
  1. coreNetworkModule
  2. coreDatabaseModule
  3. corePreferencesModule
  4. coreConfigModule
  5. secureStorageModule

Features:
  6. onboardingModule
  7. settingsModule

App:
  8. appModule  (top-level ViewModels, if any)
```

### Android Initialization (`MyApp.kt`)

```kotlin
// composeApp/src/androidMain/kotlin/com/app/budgetnote/MyApp.kt

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(
                // Core
                coreNetworkModule,
                coreDatabaseModule,
                corePreferencesModule,
                coreConfigModule,
                secureStorageModule,
                // Features
                onboardingModule,
                settingsModule,
                // App
                appModule
            )
        }
    }
}
```

### Desktop Initialization (`Main.kt`)

On Desktop, Koin is initialized directly inside `main()`, before the `Window` composable is created.

```kotlin
// composeApp/src/desktopMain/kotlin/Main.kt

fun main() = application {
    startKoin {
        modules(
            // Core
            coreNetworkModule,
            coreDatabaseModule,
            corePreferencesModule,
            coreConfigModule,
            secureStorageModule,
            // Features
            onboardingModule,
            settingsModule,
            // App
            appModule
        )
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Budget Notes Apps",
        state = rememberWindowState(width = 400.dp, height = 800.dp),
    ) {
        App()
    }
}
```

---

### iOS Initialization (`KoinHelper.kt`)

**CRITICAL:** iOS must call `doInitKoin()` from Swift **before** any UI is created.

```kotlin
// composeApp/src/iosMain/kotlin/com/app/budgetnote/KoinHelper.kt

fun doInitKoin() {
    startKoin {
        modules(
            coreNetworkModule,
            coreDatabaseModule,
            corePreferencesModule,
            coreConfigModule,
            secureStorageModule,
            onboardingModule,
            settingsModule,
            appModule
        )
    }
}
```

```swift
// iosApp/iosApp/iOSApp.swift
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinHelperKt.doInitKoin()  // MUST call before UI
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

---

## Adding a New Feature Module

When adding a new feature (e.g., `budget`), follow this checklist:

1. Create `feature/budget/di/BudgetModule.kt`
2. Register bindings in order: API → DataSource → Repository → UseCases → ViewModel
3. Add `budgetModule` to **all three** platform entry points:
   - `MyApp.kt` — Android (`composeApp/src/androidMain/`)
   - `Main.kt` — Desktop (`composeApp/src/desktopMain/`)
   - `KoinHelper.kt` — iOS (`composeApp/src/iosMain/`)

```kotlin
// feature/budget/di/BudgetModule.kt
val budgetModule = module {
    // API
    single<BudgetApiService> { BudgetApiServiceImpl(get(), get()) }

    // Data Sources
    single<BudgetDataSource.Remote> { BudgetRemoteDataSourceImpl(get()) }
    single<BudgetDataSource.Local> { BudgetLocalDataSourceImpl(get()) }

    // Repository
    single<BudgetRepository> { BudgetRepositoryImpl(get(), get()) }

    // Use Cases
    factory { GetBudgetListUseCase(get()) }
    factory { CreateBudgetUseCase(get()) }

    // ViewModel
    viewModel { BudgetViewModel(get(), get()) }
}
```
