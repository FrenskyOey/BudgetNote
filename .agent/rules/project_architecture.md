---
description: Single source of truth for project structure, layers, and dependency rules
---

# Project Architecture & Structure

## High-Level Overview
This is a Kotlin Multiplatform (KMP) project following a **Feature-First Modular Architecture**. All code resides in the `composeApp` module but maintains strict logical boundaries.

### Source Set Organization
- **commonMain**: Shared business logic, UI, and feature implementations.
- **androidMain**: Android-specific implementations.
- **iosMain**: iOS-specific implementations.
- **desktopMain**: Desktop-specific implementations.

---

## Directory Structure

### 1. Feature Layer (`feature/[name]/`)
Features are component-based and self-contained.

```kotlin
feature/[name]/
├── data/              # Implementation details
│   ├── api/           # Ktor API definitions
│   ├── dao/           # Room DAOs
│   ├── datasource/    # Data Sources (Remote/Local)
│   ├── model/         # DTOs & Entities
│   └── repository/    # Repository Implementation
├── domain/            # Business Logic (Pure Kotlin)
│   ├── model/         # Domain Models
│   ├── repository/    # Repository Interfaces
│   └── usecase/       # Interactors/Use Cases
├── di/                # Koin Module
└── ui/                # Presentation Layer
    ├── [screen]/      # Screen Composables
    └── components/    # Feature specific components
```

**Dependency Flow**: `ui` -> `domain` <- `data`

### 2. Core Layer (`core/`)
Shared infrastructure and utilities used by multiple features.

```kotlin
core/
├── data/
│   ├── remote/        # Network (Base, Util)
│   ├── local/         # Persistence (Database, Prefs)
│   └── mapper/        # Shared Mapper interfaces
├── domain/
│   ├── model/         # Shared Models (Result, AppError)
│   ├── repository/    # Shared Interfaces
│   ├── usecase/       # Shared Use Cases
│   └── config/        # AppConfig & Constants
├── di/                # Core DI Config
└── util/              # Shared Utilities (Logger, Date)
```

---

## Dependency Rules

### ✅ Allowed
- **Features** can depend on `core/`.
- **UI** can depend on `domain`.
- **Data** can depend on `domain`.
- **UI** can navigate to other features (via Navigation/Deep Link).

### ❌ Forbidden
- **Feature A** cannot import internal code from **Feature B** (Data/UI).
- **Domain** cannot import from **UI** or **Data**.
- **Data** cannot import from **UI**.

---

## File Location Guidelines

| Type | Correct Location |
| :--- | :--- |
| **Shared Model** | `core/domain/model/` |
| **Feature Model** | `feature/[name]/domain/model/` |
| **Shared Util** | `core/util/` |
| **Private Util** | `feature/[name]/data/util/` or `feature/[name]/ui/util/` |
| **DTOs** | `feature/[name]/data/model/` |
| **Exceptions** | `core/domain/model/AppException.kt` |

---

## Cross-Feature Communication

Since physical separation is not enforced by Gradle modules, discipline is required:

1.  **Shared Logic**: Move to `core`.
2.  **Shared Interface**: Define interface in `core`, implement in Feature A, inject in Feature B.
3.  **Navigation**: Pass primitive data via navigation routes.
