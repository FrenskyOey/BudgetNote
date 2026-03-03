---
description: Naming conventions, file rules, and error handling
---

# Coding Standards & Conventions

## Naming Conventions

**Features:**
- Package: `feature.[feature_name]` (e.g., `feature.onboarding`, `feature.budget`, `feature.settings`)
- Lowercase, singular preferred

**Classes:**
- PascalCase: `AuthRepository`, `BudgetRepository`
- Feature prefix optional but consistent

**Files:**
- Use cases: `[Action][Entity]UseCase.kt`
- Repositories: `[Entity]Repository.kt` (interface), `[Entity]RepositoryImpl.kt` (impl)
- DTOs: `[Entity]Dto.kt`
- Entities: `[Entity]Entity.kt`
- Modules: `[Feature]Module.kt`

**Data Layer (feature/data):**
- API Interface: `[Feature]ApiService.kt` (e.g., `AuthApiService`, `BudgetApiService`)
- DAO Interface: `[Feature]Dao.kt` (e.g., `BudgetDao`, `TransactionDao`)
- Data Source Interface: `[Feature][SourceType]DataSource.kt` (e.g., `AuthDataSource.Remote`, `AuthDataSource.Local`)
- Data Source Implementation: `[Feature][SourceType]DataSourceImpl.kt` (e.g., `AuthRemoteDataSourceImpl`, `AuthLocalDataSourceImpl`)

## File Location Rules

> [!NOTE]
> Please refer to `project_architecture.md` for detailed file location and structure rules.


## Error Handling Rules

### Use Core Error Types

```kotlin
// CORRECT - Feature uses core error types
suspend fun login(credentials: LoginCredentials): Result<User> {
    return try {
        val response = remoteDataSource.login(credentials)
        Result.Success(response.toDomain())
    } catch (e: Exception) {
        Result.Error(ApiErrorHandler.handleError(e))  // From core
    }
}

// WRONG - Feature creates own error types
sealed class AuthException : Exception()  // Should use core AppException
```

---

## API Response Pattern

### Always Use BaseResponse

All API endpoints return responses wrapped in `BaseResponse<T>` from `core.data.remote.model`. This provides a consistent structure with a `data` field and an `isSuccess` flag.

```kotlin
// API Response Structure
@Serializable
data class BaseResponse<T>(
    @SerialName("data") val data: T,
    @SerialName("is_success") val isSuccess: Boolean
)
```

**API Service Interface:**

```kotlin
// CORRECT - Return BaseResponse<T>
interface AuthApiService {
    suspend fun login(request: LoginRequest): BaseResponse<LoginResponse>
}
```

**API Service Implementation:**

```kotlin
// CORRECT - Ktor will deserialize into BaseResponse
class AuthApiServiceImpl(
    private val httpClient: HttpClient,
    private val appConfig: AppConfig
) : AuthApiService {
    override suspend fun login(request: LoginRequest): BaseResponse<LoginResponse> {
        return httpClient.post("${appConfig.baseApiUrl}auth/login") {
            setBody(request)
        }.body()
    }
}
```

**Remote Data Source:**

```kotlin
// CORRECT - Unwrap BaseResponse and check isSuccess
class AuthRemoteDataSourceImpl(
    private val apiService: AuthApiService
) : AuthDataSource.Remote {
    override suspend fun login(request: LoginRequest): LoginResponse {
        return try {
            val response = apiService.login(request)
            if (response.isSuccess) {
                response.data
            } else {
                throw AppException.AuthException("Login failed: isSuccess=false")
            }
        } catch (e: Exception) {
            throw ApiErrorHandler.handleError(e)
        }
    }
}
```

**Key Points:**
- API Service returns `BaseResponse<T>` where `T` is your response DTO
- Remote Data Source unwraps the response and validates `isSuccess`
- If `isSuccess` is `false`, throw an appropriate `AppException`
- Always catch exceptions and use `ApiErrorHandler.handleError(e)`

---

## Code Quality Rules

### Readable and Well-Named Code

```kotlin
// WRONG - Unclear naming
fun proc(d: List<T>): List<T> {
    return d.filter { it.s == 1 }.map { it.copy(s = 2) }
}

// CORRECT - Clear, descriptive naming
fun filterActiveBudgets(budgets: List<Budget>): List<Budget> {
    return budgets
        .filter { it.status == BudgetStatus.ACTIVE }
        .map { it.copy(status = BudgetStatus.ARCHIVED) }
}
```

### Small Functions (<50 lines)

```kotlin
// WRONG - Large function doing too much
fun loadAndProcessData() {
    // 100+ lines of code handling multiple responsibilities
}

// CORRECT - Single responsibility, small functions
suspend fun loadUserProfile(): Result<User> {
    return repository.getCurrentUser()
}

fun filterValidTransactions(items: List<Transaction>): List<Transaction> {
    return items.filter { it.isValid }
}

fun mapToUiState(items: List<Transaction>): TransactionUiState {
    return TransactionUiState(items = items.map { it.toUiModel() })
}
```

### Focused Files (<800 lines)

```kotlin
// WRONG - Monolithic file with multiple concerns
// BudgetScreen.kt (1200+ lines with UI, business logic, mappers)

// CORRECT - Split into focused files
// BudgetScreen.kt         - UI composables only
// BudgetViewModel.kt      - State management
// BudgetUiState.kt        - UI state models
// BudgetMapper.kt         - Domain to UI mapping
```

### No Deep Nesting (>4 levels)

```kotlin
// WRONG - Deep nesting (5+ levels)
fun processTransactions(transactions: List<Transaction>?) {
    transactions?.let { items ->
        items.forEach { item ->
            if (item.isValid) {
                item.categories.forEach { category ->
                    if (category.isActive) {
                        // Level 5+ - too deep!
                    }
                }
            }
        }
    }
}

// CORRECT - Early returns and extracted functions
fun processTransactions(transactions: List<Transaction>?) {
    if (transactions.isNullOrEmpty()) return

    transactions.filter { it.isValid }
        .flatMap { it.categories }
        .filter { it.isActive }
        .forEach { processCategory(it) }
}
```

### Proper Error Handling

```kotlin
// WRONG - Swallowing exceptions
fun fetchBudget(): Budget? {
    return try {
        api.getBudget()
    } catch (e: Exception) {
        null  // Silent failure, no context
    }
}

// CORRECT - Proper error propagation with context
suspend fun fetchBudget(): Result<Budget> {
    return try {
        val data = api.getBudget()
        Result.Success(data)
    } catch (e: Exception) {
        Logger.e("Failed to fetch budget", e)
        Result.Error(ApiErrorHandler.handleError(e))
    }
}
```

### No Debug Logging in Production

```kotlin
// WRONG - Debug logs left in code
fun processTransaction(amount: Double) {
    println("Processing transaction: $amount")  // WRONG
    Log.d("Transaction", "Amount: $amount")     // WRONG in production code
}

// CORRECT - Use proper logging with levels
fun processTransaction(amount: Double) {
    Logger.d { "Processing transaction" }  // Conditionally stripped in release
}

// Or remove entirely for sensitive financial operations
fun processTransaction(amount: Double) {
    // No logging for sensitive financial data
    transactionProcessor.process(amount)
}
```

### No Hardcoded Values

```kotlin
// WRONG - Magic numbers and hardcoded strings
fun fetchTransactions() {
    val response = api.getTransactions(limit = 20, timeout = 30000)
    if (response.code == 200) { ... }
}

// CORRECT - Named constants and configuration
object TransactionConfig {
    const val DEFAULT_PAGE_SIZE = 20
    const val API_TIMEOUT_MS = 30_000L
}

object HttpStatus {
    const val OK = 200
}

fun fetchTransactions() {
    val response = api.getTransactions(
        limit = TransactionConfig.DEFAULT_PAGE_SIZE,
        timeout = TransactionConfig.API_TIMEOUT_MS
    )
    if (response.code == HttpStatus.OK) { ... }
}
```

### Immutable Patterns (No Mutation)

```kotlin
// WRONG - Mutable state
class BudgetViewModel {
    private val _items = mutableListOf<Transaction>()

    fun addItem(item: Transaction) {
        _items.add(item)  // Mutation!
    }

    fun clearItems() {
        _items.clear()  // Mutation!
    }
}

// CORRECT - Immutable state with copy
class BudgetViewModel : ViewModel() {
    private val _state = MutableStateFlow(BudgetUiState())
    val state: StateFlow<BudgetUiState> = _state.asStateFlow()

    fun addTransaction(item: Transaction) {
        _state.update { currentState ->
            currentState.copy(transactions = currentState.transactions + item)
        }
    }

    fun clearTransactions() {
        _state.update { currentState ->
            currentState.copy(transactions = emptyList())
        }
    }
}

// CORRECT - Immutable data transformations
fun processTransactions(items: List<Transaction>): List<Transaction> {
    return items
        .filter { it.isValid }
        .map { it.copy(processed = true) }  // Creates new instances
}
```

---

# Code Quality Checklist

Before marking work complete:

### Readability:
- [ ] Code is readable and well-named (no single-letter variables except loops)
- [ ] Functions have clear, descriptive names indicating their purpose
- [ ] Variables describe what they hold, not their type

### Size Limits:
- [ ] Functions are small (<50 lines)
- [ ] Files are focused (<800 lines)
- [ ] Classes have single responsibility

### Code Structure:
- [ ] No deep nesting (>4 levels) - use early returns or extract functions
- [ ] Proper error handling with Result type
- [ ] Errors logged with appropriate context

### Clean Code:
- [ ] No println/console.log/Log.d statements in production code
- [ ] No hardcoded values - use named constants
- [ ] No magic numbers - define meaningful constant names
- [ ] Use immutable collections where possible
- [ ] StateFlow updates use `update {}` pattern

### Documentation:
- [ ] **Context Check**: Did you change architecture or patterns? If yes, activate `documentation_maintenance` skill to update `.agent/rules`.
