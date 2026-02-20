---
description: Technology stack and library choices
---

# Technology Stack

## Core Technologies

- **Kotlin Multiplatform**: The backbone of the application, enabling 100% shared logic and UI potential.
- **Compose Multiplatform**: For declarative UI sharing across platforms.

## Resources
- **Compose Multiplatform Resources**: Official Jetbrains library for sharing strings, images, and fonts (`composeResources`).

## Dependency Injection
- **Koin**: Chosen for its native Kotlin support and ease of use in KMP.
    - `koin-core` for shared logic.
    - `koin-compose` for UI injection.
    - `koin-android` for Android-specific contexts.

## Networking
- **Ktor**: Multiplatform asynchronous HTTP client.
    - `ktor-client-core`: Shared interface.
    - `ktor-client-content-negotiation`: JSON Serialization.
    - `ktor-client-logging`: Debugging.

## Database
- **Room**: Recently supported in KMP (via SQLite bundled key). Provides robust ORM with compile-time verification.

## Local Storage
- **DataStore (Preferences)**: For storing simple key-value pairs (settings, flags).

## Asynchronous Programming
- **Kotlinx Coroutines**: For structured concurrency.

## Date & Time
- **Kotlinx DateTime**: Multiplatform date/time handling.

## Image Loading
- **Coil 3**: Multiplatform image loading for Compose.

## Build System
- **Gradle Version Catalogs**: For centralized dependency management (`libs.versions.toml`).
- **Kotlin DSL**: type-safe build scripts.
