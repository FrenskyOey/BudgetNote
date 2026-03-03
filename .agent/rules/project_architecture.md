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

```
feature/[name]/
├── data/              # Implementation details
│   ├── api/           # Ktor API service interface & impl
│   ├── datasource/    # Data Sources (Remote/Local impls)
│   ├── mapper/        # DTO ↔ Domain mappers
│   ├── model/         # DTOs, Entities, Requests, Responses
│   └── repository/    # Repository Implementation
├── domain/            # Business Logic (Pure Kotlin)
│   ├── model/         # Domain Models
│   ├── repository/    # Repository Interfaces
│   └── usecase/       # Interactors/Use Cases
├── di/                # Koin Module
└── ui/                # Presentation Layer
    ├── screen/        # Screen Composables
    ├── components/    # Feature-specific reusable components
    ├── state/         # UI State, Events (Intent), Effects
    └── viewmodel/     # ViewModel
```

> [!NOTE]
> - `feature/data/` uses `mapper/` (not `dao/`) — DAOs belong in `core/data/local/database/`.
> - `feature/ui/` always has `state/` for `UiState`, `Event`, and `Effect` classes, and `viewmodel/` for the ViewModel.

**Dependency Flow**: `ui` → `domain` ← `data`

### 2. Core Layer (`core/`)
Shared infrastructure and utilities used by multiple features.

```
core/
├── components/        # Shared reusable UI components (CoreButton, CoreTextInput, etc.)
├── data/
│   ├── remote/        # Network utilities (ApiErrorHandler, JsonSerializer, BaseResponse)
│   ├── local/         # Persistence (AppDatabase, DatabaseBuilder, PreferencesManager)
│   ├── mapper/        # Shared Mapper interfaces
│   └── repository/    # Core repository implementations (e.g., SessionRepositoryImpl)
├── domain/
│   ├── model/         # Shared Models (Result, AppException, PaginatedData)
│   ├── repository/    # Shared Repository Interfaces (e.g., SessionRepository)
│   └── config/        # AppConfig & Constants
├── di/                # Core DI modules (coreNetworkModule, coreDatabaseModule, etc.)
├── mvi/               # MVI base classes (MviViewModel, MviView, ViewEffect)
├── navigation/        # Shared navigation routes/helpers
├── theme/             # Design system (Color, Typography, Dimens, Theme)
└── util/              # Shared Utilities (TimeUtil, HashUtil)
```

> [!NOTE]
> `core/domain/` does **not** have a `usecase/` directory — shared use cases do not exist yet. If shared use cases are needed in the future, add them to `core/domain/usecase/`.

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
| **Feature Domain Model** | `feature/[name]/domain/model/` |
| **Feature DTO / Entity** | `feature/[name]/data/model/` |
| **Feature Mapper** | `feature/[name]/data/mapper/` |
| **Shared Util** | `core/util/` |
| **Shared UI Component** | `core/components/` |
| **Feature UI Component** | `feature/[name]/ui/components/` |
| **UI State / Event / Effect** | `feature/[name]/ui/state/` |
| **ViewModel** | `feature/[name]/ui/viewmodel/` |
| **Exceptions** | `core/domain/model/AppException.kt` |
| **DAOs** | `core/data/local/database/` |

---

## Cross-Feature Communication

Since physical separation is not enforced by Gradle modules, discipline is required:

1. **Shared Logic**: Move to `core`.
2. **Shared Interface**: Define interface in `core`, implement in Feature A, inject in Feature B.
3. **Navigation**: Pass primitive data via navigation routes.
