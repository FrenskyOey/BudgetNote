---
description: Dependency injection setup and rules using Koin
---

# Dependency Injection Rules

## Feature Modules

In KMP, feature modules are defined in `commonMain` and can use `factoryOf`, `singleOf` for conciseness.

```kotlin
// compostApp/src/commonMain/kotlin/feature/news/di/NewsModule.kt

val newsModule = module {
    // API
    single<NewsApiService> { NewsApiServiceImpl(get()) }
    
    // DAO
    single { get<AppDatabase>().newsFeedDao() }
    
    // Repository
    single<NewsFeedRepository> { NewsFeedRepositoryImpl(get(), get()) }
    
    // Use Cases
    factory { GetNewsFeedUseCase(get()) }
    
    // ViewModels (in appModule or feature module)
    // factoryOf(::NewsFeedViewModel) 
}
```

## Module Organization & Initialization

The project uses platform-specific entry points to initialize Koin with a shared list of modules.

### Shared Module List
The modules are typically aggregated in the initialization block or a shared variable.

Common Modules:
- `coreNetworkModule`
- `coreDatabaseModule`
- `corePreferencesModule`
- `coreConfigModule`
- `newsModule`
- `settingsModule`
- `appModule` (ViewModels)

### Android Initialization (`MyApp.kt`)

```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(
                coreNetworkModule,
                coreDatabaseModule,
                corePreferencesModule,
                coreConfigModule, // App Config
                
                // Features
                newsModule,
                settingsModule,
                
                // Main App
                appModule
            )
        }
    }
}
```

### iOS Initialization (`KoinHelper.kt`)

**CRITICAL:** iOS apps must call `doInitKoin()` (or similar helper) from Swift before any UI is created.

```kotlin
// composeApp/src/iosMain/kotlin/com/interview/prep/kmp_learn/KoinHelper.kt
fun doInitKoin() {
    startKoin {
        modules(
            coreNetworkModule,
            coreDatabaseModule,
            corePreferencesModule,
            coreConfigModule,
            newsModule,
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
