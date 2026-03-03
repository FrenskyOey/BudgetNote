---
description: Technology stack and library choices
---

# Technology Stack

## Core Technologies

- **Kotlin Multiplatform**: Backbone of the application, enabling shared logic and UI across Android, iOS, and Desktop.
- **Compose Multiplatform**: Declarative UI shared across platforms.
- **Target Platforms**: Android (minSdk 24, targetSdk 35), iOS (arm64, simulatorArm64), Desktop (JVM 17)

> [!NOTE]
> All versions are managed centrally in `gradle/libs.versions.toml`.

---

## UI
- `compose-material3` — Primary UI component library
- `compose-material-icons-extended` — Extended icon set
- `compose-foundation`, `compose-ui`, `compose-runtime` — Core Compose primitives
- `androidx-lifecycle-viewmodelCompose` / `runtimeCompose` — ViewModel & lifecycle integration with Compose

## Navigation
- `androidx-navigation-compose` — Type-safe Compose Navigation for all platforms

## Resources
- `compose-components-resources` — Shared strings, images, and fonts via `Res` object

## Dependency Injection
- `koin-core` — Core DI (shared)
- `koin-compose` — `koinInject()` for Compose
- `koin-compose-viewmodel` — `koinViewModel()` for Compose screens
- `koin-android` — Android `androidContext()` support

## Networking
- `ktor-client-core` — Multiplatform async HTTP client
- `ktor-client-content-negotiation` + `ktor-serialization-kotlinx-json` — JSON serialization
- `ktor-client-logging` — Request/response debug logging
- `ktor-client-auth` — Bearer token authentication plugin
- `ktor-client-android` — Engine for Android & Desktop (JVM)
- `ktor-client-darwin` — Engine for iOS
- `ktor-client-mock` — Mock engine for tests

## Backend / Cloud
- `supabase-auth` (`auth-kt`) — Authentication (login, session)
- `supabase-functions` (`functions-kt`) — Supabase Edge Functions client

## Database
- `androidx-room-runtime` + `sqlite-bundled` — KMP ORM with bundled SQLite driver
- `androidx-room-compiler` — KSP annotation processor (applied via `kspAndroid`, `kspDesktop`, `kspIos*`)

## Local Storage
- `multiplatform-settings` — Key-value storage; secure instance injected via `named("secure")` Koin qualifier
- `multiplatform-settings-no-arg` — No-arg factory for Desktop
- `androidx-datastore-preferences` — Preferences DataStore for structured key-value data
- `androidx-security-crypto` — `EncryptedSharedPreferences` for Android secure storage

## Serialization
- `kotlinx-serialization-json` — JSON serialization for DTOs and API models

## Asynchronous Programming
- `kotlinx-coroutines-core` — Structured concurrency (shared)
- `kotlinx-coroutines-android` — Android `Main` dispatcher
- `kotlinx-coroutines-swing` — Desktop (Swing) `Main` dispatcher
- `kotlinx-coroutines-test` — Test utilities

## Date & Time
- `kotlinx-datetime` — Multiplatform date/time handling

## Image Loading
- `coil-compose` + `coil-network-ktor3` — Multiplatform image loading with Ktor network fetcher

## Utilities
- `okio` — File I/O and buffer utilities

## Build Tooling
- **Gradle Version Catalogs** (`libs.versions.toml`) — Centralized dependency management
- **KSP** — Kotlin Symbol Processing for Room code generation
- **BuildKonfig** — Injects `local.properties` values (API URLs, Supabase keys) into a generated `BuildConfig` per flavor/target
- **Android Product Flavors** — `staging` and `production` build variants with separate config
