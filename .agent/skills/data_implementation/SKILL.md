---
name: data_implementation
description: Enforces standardized Data Layer patterns. Use when implementing Repositories, Data Sources, or API Services.
---

# Data Layer Implementation Skill

Use this skill when implementing the Data Layer (Repositories, Data Sources, API Services) to ensure consistency and robustness.

## 1. Directory & Package Structure

Mandatory structure for `feature/[name]/data/`:

```text
data/
├── api/             # API Service definitions & implementations
│   ├── [Name]ApiService.kt      (Interface)
│   └── [Name]ApiServiceImpl.kt  (Implementation)
├── datasource/      # Data Source interfaces & implementations
│   ├── [Name]DataSource.kt      (Parent Interface)
│   ├── remote/
│   │   └── [Name]RemoteDataSourceImpl.kt
│   └── local/
│       └── [Name]LocalDataSourceImpl.kt
├── model/           # Data Transfer Objects & Entities
│   ├── entity/      # Local database/storage models
│   ├── request/     # Network request bodies
│   ├── response/    # Network response bodies
│   └── mapper/      # Mappers (DTO <-> Domain <-> Entity)
└── repository/      # Repository Implementation
    └── [Name]RepositoryImpl.kt
```

## 2. API Service Pattern

**Decouple Networking Logic**:
- Define a pure interface `[Feature]ApiService`.
- Implement it in `[Feature]ApiServiceImpl` taking `HttpClient`.
- **NEVER** use `HttpClient` directly in `RemoteDataSource`.

```kotlin
// Interface
interface AuthApiService {
    suspend fun login(request: LoginRequest): LoginResponse
}

// Implementation
class AuthApiServiceImpl(private val client: HttpClient) : AuthApiService { ... }
```

## 3. Data Source Pattern

**Nested Interfaces**:
- Use a single parent interface to group Remote and Local definitions.

```kotlin
interface AuthDataSource {
    interface Remote {
        suspend fun login(request: LoginRequest): LoginResponse
    }
    interface Local {
        fun saveToken(token: String)
    }
}
```

## 4. Error Handling Rules

**Trigger**: When handling exceptions in Data Layer components.

- **RemoteDataSource**:
    - **Catch** network exceptions.
    - **Map** to `AppException` (using `ApiErrorHandler` if applicable).
    - **THROW** the `AppException`.
    - *Do NOT return Result.Error here.*

```kotlin
// RemoteDataSourceImpl
try {
    // ... api call ...
} catch (e: Exception) {
    throw ApiErrorHandler.handleError(e) // THROW!
}
```

- **Repository**:
    - **Catch** exceptions from Data Sources.
    - **Map** unknown exceptions to `AppException.Unknown`.
    - **RETURN** `Result.Error`.

```kotlin
// RepositoryImpl
try {
    val response = remoteDataSource.login(...)
    Result.Success(response.toDomain())
} catch (e: Exception) {
    // Propagate existing AppExceptions (from DataSource) or wrap others
    val error = (e as? AppException) ?: AppException.UnknownError(e.message, e)
    Result.Error(error) // RETURN!
}
```

## 6. Command-Query Separation (CQS) Pattern

### When to Use CQS

**Decision Tree:**
```
Does Repository return Flow?
├─ YES → Is Database the source of truth?
│         ├─ YES → Use CQS Pattern ✅
│         └─ NO → Standard Repository
└─ NO → Standard Repository
```

**Use CQS Pattern When:**
- ✅ Database is the source of truth
- ✅ Repository returns `Flow` that emits updates
- ✅ Data is fetched from remote and stored locally
- ✅ Need to separate observation from refresh action

**Examples:** News Detail, User Profile, Article List with caching

**Don't Use CQS When:**
- ❌ Simple API call without local storage
- ❌ No Flow emissions needed
- ❌ Direct return of API response
- ❌ No database involvement

**Examples:** Login, Submit Form, One-time API calls

---

### CQS Pattern Structure

**Repository Interface:**
```kotlin
interface NewsDetailRepository {
    // QUERY: Pure observation (no side effects)
    // Returns Flow from local database
    fun getNewsDetail(id: Int): Flow<NewsDetail?>
    
    // COMMAND: Explicit action (side effects allowed)
    // Fetches from remote, updates local database
    suspend fun refreshNewsDetail(id: Int): Result<Unit>
}
```

**Repository Implementation:**
```kotlin
class NewsDetailRepositoryImpl(
    private val remoteDataSource: NewsDetailDataSource.Remote,
    private val localDataSource: NewsDetailDataSource.Local
) : NewsDetailRepository {
    
    // QUERY: Only observe local data
    override fun getNewsDetail(id: Int): Flow<NewsDetail?> {
        return localDataSource.getNewsDetail(id)
            .map { it?.toDomain() }
    }
    
    // COMMAND: Fetch from network, update local
    override suspend fun refreshNewsDetail(id: Int): Result<Unit> {
        return try {
            val response = remoteDataSource.getNewsDetail(id)
            localDataSource.upsertNewsDetail(response.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            val error = (e as? AppException) ?: AppException.UnknownError(e.message, e)
            Result.Error(error)
        }
    }
}
```

**Benefits:**
- No redundant network calls on re-collection
- Explicit control over when data refreshes
- Multiple collectors don't trigger multiple fetches
- Perfect for Pull-to-Refresh scenarios
- Prevents accidental fetches on screen rotation

---

### Standard Repository Pattern (Non-CQS)

**Use for simple API calls without local storage:**

```kotlin
interface AuthRepository {
    // Single operation: call API, return result
    suspend fun login(email: String, password: String): Result<LoginModel>
}

class AuthRepositoryImpl(
    private val remoteDataSource: AuthDataSource.Remote
) : AuthRepository {
    
    override suspend fun login(email: String, password: String): Result<LoginModel> {
        return try {
            val response = remoteDataSource.login(LoginRequest(email, password))
            Result.Success(response.toDomain())
        } catch (e: Exception) {
            val error = (e as? AppException) ?: AppException.UnknownError(e.message, e)
            Result.Error(error)
        }
    }
}
```

---

## 7. Implementation Workflow

1.  **Define Models**: Create all `request`, `response`, `entity` classes first.
2.  **Define API Interface**: Create the contract.
3.  **Define Data Sources**: Update the definitions.
4.  **TDD**: Write tests for `RemoteDataSourceImplTest` and `RepositoryImplTest` **before** implementation.
5.  **Implement**:
    - `ApiServiceImpl`
    - `RemoteDataSourceImpl` & `LocalDataSourceImpl`
    - `RepositoryImpl`
    - `Mappers`
6.  **DI**: Register all in `[Feature]Module`.

---

## 8. Interface Change Propagation

**Trigger**: Any time you add, remove, or rename a method on a DAO or DataSource interface.

When an interface changes, update **all** of the following in order — missing any one causes compilation errors:

| # | File | Action |
|---|---|---|
| 1 | `*Dao.kt` | Add/update the `@Query` or `@Insert` method |
| 2 | `[Name]DataSource.kt` | Add/update the method on `Local` or `Remote` interface |
| 3 | `[Name]LocalDataSourceImpl.kt` | Implement the new method, delegate to DAO |
| 4 | `FakeNewsRemoteKeysDao.kt` (test) | Add/update in-memory implementation |
| 5 | `Fake[Name]LocalDataSource.kt` (test) | Add/update in-memory implementation |
| 6 | `[Name]RepositoryImplTest.kt` | Update any tests that depend on the changed method |

**Example** — adding `getRemoteKeyByOrderIndex(orderIndex: Int)`:
```
1. NewsRemoteKeysDao          → add @Query
2. NewsDataSource.Local       → add method to interface
3. NewsLocalDataSourceImpl    → implement, delegate to DAO
4. FakeNewsRemoteKeysDao      → implement with in-memory list lookup
5. FakeNewsLocalDataSource    → implement, delegate to fake DAO
6. NewsFeedRepositoryImplTest → update tests using the new method
```

> ⚠️ Never leave a fake implementation unimplemented (throwing `NotImplementedError`). Fakes must behave correctly for tests to be meaningful.
