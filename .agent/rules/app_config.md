---
description: Environment configuration and AppConfig usage
---

# Environment Configuration (AppConfig)

## CRITICAL: All Remote Services MUST Use AppConfig

**Rule:** Every API service, remote data source, or network client MUST inject and use `AppConfig` for base URLs and environment-specific settings.

## AppConfig Structure

```kotlin
// ✅ CORRECT - AppConfig in core/domain/config
core/domain/config/
├── AppConfig.kt                    # Interface (commonMain)
├── AndroidAppConfig.kt             # Android implementation (androidMain)
└── IosAppConfig.kt                 # iOS implementation (iosMain)

// AppConfig.kt (commonMain)
interface AppConfig {
    val baseApiUrl: String
    val flavorName: String
    // Add other environment-specific configs here
}

expect fun createAppConfig(): AppConfig
```

## AppConfig Best Practices

**✅ DO:**
- Always inject `AppConfig` into services that need URLs or environment settings
- Use `AppConfig` for: base URLs, API keys, feature flags, environment names
- Keep `AppConfig` in `core/domain/config/`
- Provide platform-specific implementations via `expect/actual`
- Include `coreConfigModule` in Koin initialization

**❌ DON'T:**
- Hardcode URLs anywhere in the codebase
- Create feature-specific config classes (use shared `AppConfig`)
- Put configuration in companion objects or constants
- Skip injecting `AppConfig` into remote services
