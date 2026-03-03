---
trigger: always_on
description: Testing guidelines and code review checklist
---

# Testing Rules

## Test Structure

Tests mirror the source package structure under `commonTest`:

```
composeApp/src/commonTest/kotlin/
└── feature/
    └── onboarding/
        ├── domain/
        │   └── usecase/
        │       ├── LoginUseCaseTest.kt
        │       ├── LogoutUseCaseTest.kt
        │       ├── ValidateEmailUseCaseTest.kt
        │       ├── ValidatePasswordUseCaseTest.kt
        │       ├── CheckLoginStatusUseCaseTest.kt
        │       └── GetCurrentUserUseCaseTest.kt
        ├── data/
        │   ├── repository/
        │   │   └── AuthRepositoryImplTest.kt
        │   └── datasource/
        │       ├── local/
        │       │   └── AuthLocalDataSourceImplTest.kt
        │       └── remote/
        │           └── AuthRemoteDataSourceImplTest.kt
        └── ui/
            └── viewmodel/
                └── LoginViewModelTest.kt
```

---

## Test Coroutine Pattern

Use `runTest` from `kotlinx.coroutines.test` for suspend functions. For use cases that don't need a full coroutine scope, a `runBlockingTest` helper defined locally is acceptable.

```kotlin
// ✅ PREFERRED — Repository and DataSource tests
@Test
fun `login success saves user and returns success`() = runTest {
    // Arrange
    remoteDataSource.response = LoginResponse(userData, true)
    // Act
    val result = repository.login(credentials)
    // Assert
    assertIs<Result.Success<*>>(result)
}

// ✅ ACCEPTABLE — UseCase tests (simpler coroutine setup)
@Test
fun `should return success when credentials are valid`() = runBlockingTest {
    val result = useCase(email, password)
    assertTrue(result is Result.Success)
}

// Local helper within the test class
private fun runBlockingTest(block: suspend () -> Unit) {
    kotlinx.coroutines.runBlocking { block() }
}
```

---

## Test Isolation

```kotlin
// ✅ CORRECT - Test only the class under test using fakes
class LoginUseCaseTest {
    @Test
    fun `should return success when credentials are valid`() = runBlockingTest {
        val fakeRepository = FakeAuthRepository()
        val useCase = LoginUseCase(fakeRepository, ValidateEmailUseCase(), ValidatePasswordUseCase())
        val result = useCase("user@example.com", "Test123")
        assertTrue(result is Result.Success)
    }
}

// ❌ WRONG - Test crosses feature boundaries
class LoginUseCaseTest {
    private lateinit var settingsRepository: SettingsRepository  // WRONG — different feature!
}
```

---

## Fake Implementation Patterns

Fakes are **hand-written** and defined **in the same test file** as the class under test. No mocking libraries (MockK etc.) are used.

### Fake for UseCase Tests (defined privately in the test class)

```kotlin
class LoginUseCaseTest {
    // ...tests...

    private class FakeAuthRepository(
        private val simulateNetworkError: Boolean = false,
        private val simulateServerError: Boolean = false
    ) : AuthRepository {
        var lastCredentials: LoginCredentials? = null

        override suspend fun login(credentials: LoginCredentials): Result<User> {
            lastCredentials = credentials
            return when {
                simulateNetworkError -> Result.Error(AppException.NetworkError("Network failure"))
                simulateServerError -> Result.Error(AppException.ServerError(500, "Server error"))
                else -> Result.Success(User(userId = 1, userName = "Test User", token = "fake_token"))
            }
        }

        override suspend fun logout(): Result<Unit> = Result.Success(Unit)
        override suspend fun getCurrentUser(): Result<User?> = Result.Success(null)
        override suspend fun isLoggedIn(): Boolean = false
    }
}
```

### Fake for Repository Tests (defined at the top of the test file)

```kotlin
// Defined at file scope so both test class and helpers can use them
class FakeAuthRemoteDataSource : AuthDataSource.Remote {
    var response: LoginResponse? = null
    var thrownException: Exception? = null
    var lastLoginRequest: LoginRequest? = null

    override suspend fun login(request: LoginRequest): LoginResponse {
        lastLoginRequest = request
        thrownException?.let { throw it }
        return response ?: throw IllegalStateException("Response not set")
    }
}

class FakeAuthLocalDataSource : AuthDataSource.Local {
    var savedUser: UserEntity? = null
    var token: String? = null
    // ... implement all interface methods ...
}

class AuthRepositoryImplTest {
    private val remoteDataSource = FakeAuthRemoteDataSource()
    private val localDataSource = FakeAuthLocalDataSource()
    private val repository = AuthRepositoryImpl(remoteDataSource, localDataSource)
    // ...tests...
}
```

---

## Fake Naming Convention

**Problem**: Multiple test files in the same package with the same `Fake*` class name cause compilation conflicts.

**Solution**: Name fakes after the **operation** being tested.

```kotlin
// ❌ BAD: Same name in two test files → compilation error
// In LoginUseCaseTest.kt
class FakeAuthRepository : AuthRepository { ... }
// In LogoutUseCaseTest.kt
class FakeAuthRepository : AuthRepository { ... }  // DUPLICATE!

// ✅ GOOD: Operation-specific names
// In LoginUseCaseTest.kt
class FakeLoginAuthRepository : AuthRepository { ... }
// In LogoutUseCaseTest.kt
class FakeLogoutAuthRepository : AuthRepository { ... }
```

**Naming Pattern**: `Fake[Operation][Feature][Component]`

**Examples**:
- `FakeAuthRepository` (unique in file scope — fine for repository tests)
- `FakeLoginAuthRepository` (private in use case test)
- `FakeLogoutAuthRepository` (private in use case test)
- `FakeAuthRemoteDataSource`, `FakeAuthLocalDataSource`

---

## Test Assertions

Use `kotlin.test` assertions — no external assertion libraries:

```kotlin
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertIs
import kotlin.test.assertFalse
import kotlin.test.assertNull

assertTrue(result is Result.Success)
assertIs<Result.Error>(result)
assertEquals("expected", actual)
assertFalse(repository.isLoggedIn())
```

---

# Code Review Checklist

Before accepting code:

### Architecture:
- [ ] Features don't depend on each other
- [ ] Features only use `core/` for shared utilities
- [ ] Each feature has `data/domain/di` structure
- [ ] Each feature has its own Koin module

### Code Quality:
- [ ] Follows Clean Architecture within feature
- [ ] Uses Koin for dependency injection
- [ ] Error handling uses `core.domain.model.Result` and `AppException`
- [ ] Maps between DTOs/Entities and Domain models
- [ ] Uses `suspend` functions for async operations
- [ ] Uses MVI state management (`State`/`Event`/`Effect`)
- [ ] No hardcoded values
- [ ] Uses design system (`core/theme/`)

### Feature Isolation:
- [ ] No imports from other features' internal packages
- [ ] Can be removed without breaking other features
- [ ] Has its own tests

---

# Regression & Impact Analysis

## Trigger
**ALWAYS** perform this check when modifying existing logic in:
- **Domain Layer**: UseCases, Models
- **Data Layer**: Repositories, DataSources
- **Presentation Layer**: ViewModels, State/Effect/Event

## Protocol

1. **Identify Dependents** — Before writing code, search for usages and tests:
   - `grep_search(Query="<ComponentName>", ...)`
   - `find_by_name(Pattern="*Test.kt", ...)`

2. **Analyze Impact**:
   - **Case A (Refactor)**: Internal logic change, output same → existing tests MUST pass without modification.
   - **Case B (Logic Change)**: Business rule change → identify conflicting tests and UPDATE them.
   - **Case C (Deprecation)**: Feature removed → propose DELETING the tests.

3. **Notify User** if tests need updates or deletion before proceeding.