---
trigger: always_on
description: Testing guidelines and code review checklist
---

# Testing Rules

## Test Structure

```kotlin
// Tests mirror the source structure
composeApp/src/commonTest/kotlin/
├── core/
│   └── data/
│       └── util/
│           └── ApiErrorHandlerTest.kt
└── feature/
    ├── news/
    │   ├── domain/
    │   │   └── usecase/
    │              └── GetNewsFeedUseCaseTest.kt
    │   └── data/
    │       └── repository/
    │           └── NewsFeedRepositoryImplTest.kt
    │       └── datasource/
    │           └──local
    │           |     └── NewsLocalDataSourceImpl.kt
    │           └──remote
    │                 └── NewsRemoteDataSourceImpl.kt
    └── ...../
        └── domain/
        └── data/
```

## Test Isolation

```kotlin
// ✅ CORRECT - Test only the feature being tested
class GetNewsFeedUseCaseTest {
    private lateinit var fakeRepository: FakeNewsFeedRepository
    private lateinit var useCase: GetNewsFeedUseCase
    
    @Before
    fun setup() {
        fakeRepository = FakeNewsFeedRepository()
        useCase = GetNewsFeedUseCase(fakeRepository)
    }
}

// ❌ WRONG - Test depends on another feature
class GetNewsFeedUseCaseTest {
    private lateinit var settingsRepository: SettingsRepository  // WRONG!
}
```

## Test Fake Naming Convention

**Problem**: Multiple test files in the same package with duplicate `FakeRepository` class names cause compilation conflicts.

**Solution**: Name fakes after the operation being tested.

```kotlin
// ❌ BAD: Generic name used in multiple test files
// In GetNewsDetailUseCaseTest.kt
class FakeNewsDetailRepository : NewsDetailRepository { ... }

// In RefreshNewsDetailUseCaseTest.kt
class FakeNewsDetailRepository : NewsDetailRepository { ... }
// ^ COMPILATION ERROR: Duplicate class name!

// ✅ GOOD: Operation-specific names
// In GetNewsDetailUseCaseTest.kt
class FakeGetNewsDetailRepository : NewsDetailRepository { ... }

// In RefreshNewsDetailUseCaseTest.kt
class FakeRefreshNewsDetailRepository : NewsDetailRepository { ... }
```

**Naming Pattern**: `Fake[Operation][Feature][Component]`

**Examples**:
- `FakeGetNewsDetailRepository`
- `FakeRefreshNewsDetailRepository`
- `FakeLoginRepository`
- `FakeGetNewsFeedRepository`

**Test File Organization**:
- One test file per use case/operation
- One fake repository per test file
- Fake named after the operation being tested
- Avoids naming conflicts in the same package


# Code Review Checklist

Before accepting code, verify:

### Architecture:
- [ ] Features don't depend on each other
- [ ] Features only use core for shared utilities
- [ ] Each feature has data/domain/di structure
- [ ] Shared utilities are in core, not features
- [ ] Each feature has its own Koin module

### Code Quality:
- [ ] Follows Clean Architecture within feature
- [ ] Uses dependency injection (Koin)
- [ ] Has proper error handling with core Result
- [ ] Maps between DTOs/Entities and Domain models
- [ ] Uses suspend functions for async operations
- [ ] Has proper state management
- [ ] Follows Kotlin coding conventions
- [ ] No hardcoded values
- [ ] Uses theme helpers
- [ ] Has appropriate documentation

### Feature Isolation:
- [ ] No imports from other features
- [ ] Can be removed without breaking other features
- [ ] Has its own tests

# Regression & Impact Analysis

## Trigger
**ALWAYS** perform this check when modifying existing logic in:
- **Domain Layer**: UseCases, Models
- **Data Layer**: Repositories, DataSources
- **Presentation Layer**: ViewModels, State Reducers

## Protocol

1.  **Identify Dependents**:
    Before writing code, search for usages and tests:
     - `grep_search(Query="<ComponentName>", ...)`
     - `find_by_name(Pattern="*Test.kt", ...)`

2.  **Analyze Impact**:
    - **Case A (Refactor)**: Internal logic change, output remains same.
      - **Constraint**: Existing tests MUST pass without modification.
    - **Case B (Logic Change)**: Business rule change (e.g. min age 18 -> 21).
      - **Action**: You MUST identify conflicting tests and plan to UPDATE them.
    - **Case C (Deprecation)**: Feature removed.
      - **Action**: Propose DELETING valid tests.

3.  **Notify User**:
    - If tests need updates/deletion, list them in the Plan or Response.
    - Ask for confirmation if deleting tests.