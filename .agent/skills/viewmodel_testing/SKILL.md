---
name: ViewModel Testing
description: Enforces standardized ViewModel testing patterns using coroutines, test dispatchers, and proper state/effect verification. Use when creating tests for any *ViewModel.kt file.
---

# ViewModel Testing Skill

## When to Use
- Creating tests for any file ending in `ViewModel.kt`
- Testing StateFlow, SharedFlow, or Channel-based state management
- Testing MVI pattern (State/Event/Effect)

---

## Test Structure Template

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class MyViewModelTest {
    // 1. Test dispatcher for controlling coroutine execution
    private val testDispatcher = StandardTestDispatcher()
    
    // 2. Fake dependencies (repositories, use cases)
    private lateinit var fakeRepository: FakeMyRepository
    private lateinit var fakeUseCase: FakeMyUseCase
    
    // 3. ViewModel under test
    private lateinit var viewModel: MyViewModel
    
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Initialize fakes
        fakeRepository = FakeMyRepository()
        fakeUseCase = FakeMyUseCase()
        
        // Create ViewModel with fakes
        viewModel = MyViewModel(fakeRepository, fakeUseCase)
    }
    
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
```

---

## Testing State (StateFlow)

### Pattern 1: Synchronous State Updates
```kotlin
@Test
fun `event updates state synchronously`() = runTest(testDispatcher) {
    // Given
    val initialState = viewModel.state.value
    
    // When
    viewModel.onEvent(MyEvent.UpdateField("new value"))
    
    // Then - state updates immediately
    assertEquals("new value", viewModel.state.value.field)
}
```

### Pattern 2: Async State Updates (with UseCase)
```kotlin
@Test
fun `async operation updates state`() = runTest(testDispatcher) {
    // Given
    fakeRepository.setResult(Result.Success(data))
    
    // When
    viewModel.onEvent(MyEvent.LoadData)
    
    // Advance coroutines until idle
    advanceUntilIdle()
    
    // Then
    assertEquals(data, viewModel.state.value.data)
    assertFalse(viewModel.state.value.isLoading)
}
```

### Pattern 3: Debounced/Delayed Updates
```kotlin
@Test
fun `debounced validation triggers after delay`() = runTest(testDispatcher) {
    // When
    viewModel.onEvent(MyEvent.FieldChanged("test"))
    
    // Advance time by debounce duration
    advanceTimeBy(500)
    
    // Then
    assertNotNull(viewModel.state.value.validationError)
}
```

---

## Testing Effects (Channel/SharedFlow)

### Pattern: Collect Effects in Test
```kotlin
@Test
fun `event emits effect`() = runTest(testDispatcher) {
    // Collect effects in a list
    val effects = mutableListOf<MyEffect>()
    val job = launch(testDispatcher) {
        viewModel.effect.collect { effects.add(it) }
    }
    
    // When
    viewModel.onEvent(MyEvent.ShowMessage)
    advanceUntilIdle()
    
    // Then
    assertEquals(1, effects.size)
    assertTrue(effects[0] is MyEffect.ShowSnackbar)
    
    // Cleanup
    job.cancel()
}
```

---

## Creating Test Doubles

### Fake Repository Pattern
```kotlin
class FakeMyRepository : MyRepository {
    private var result: Result<Data>? = null
    var lastCalledWith: String? = null
    
    fun setResult(result: Result<Data>) {
        this.result = result
    }
    
    override suspend fun getData(param: String): Result<Data> {
        lastCalledWith = param
        return result ?: Result.Error(AppException.Unknown("Not set"))
    }
}
```

### Fake UseCase Pattern
```kotlin
class FakeMyUseCase : MyUseCase {
    private var shouldSucceed = true
    var invokeCount = 0
    
    fun setShouldSucceed(value: Boolean) {
        shouldSucceed = value
    }
    
    override suspend fun invoke(param: String): Result<Data> {
        invokeCount++
        return if (shouldSucceed) {
            Result.Success(Data("test"))
        } else {
            Result.Error(AppException.ValidationError("Invalid"))
        }
    }
}
```

---

## Common Test Scenarios

### 1. Initial State
```kotlin
@Test
fun `initial state is correct`() {
    assertEquals("", viewModel.state.value.field)
    assertFalse(viewModel.state.value.isLoading)
    assertNull(viewModel.state.value.error)
}
```

### 2. Loading State
```kotlin
@Test
fun `loading state is set during async operation`() = runTest(testDispatcher) {
    fakeRepository.setDelay(1000) // Simulate slow operation
    
    viewModel.onEvent(MyEvent.LoadData)
    
    // Check loading is true before completion
    assertTrue(viewModel.state.value.isLoading)
    
    advanceUntilIdle()
    
    // Check loading is false after completion
    assertFalse(viewModel.state.value.isLoading)
}
```

### 3. Error Handling
```kotlin
@Test
fun `error from repository updates error state`() = runTest(testDispatcher) {
    val error = AppException.NetworkError("No connection")
    fakeRepository.setResult(Result.Error(error))
    
    viewModel.onEvent(MyEvent.LoadData)
    advanceUntilIdle()
    
    assertEquals("No connection", viewModel.state.value.error)
}
```

### 4. Validation
```kotlin
@Test
fun `invalid input shows validation error`() = runTest(testDispatcher) {
    viewModel.onEvent(MyEvent.FieldChanged(""))
    advanceUntilIdle()
    
    // Use contains() for flexible error message matching
    val error = viewModel.state.value.fieldError
    assertTrue(error != null && error.contains("cannot be empty"))
}
```

---

## Common Pitfalls & Solutions

### ❌ Pitfall 1: Not Using testDispatcher in launch
```kotlin
// WRONG
val job = launch { // Uses default dispatcher
    viewModel.effect.collect { effects.add(it) }
}

// CORRECT
val job = launch(testDispatcher) {
    viewModel.effect.collect { effects.add(it) }
}
```

### ❌ Pitfall 2: Checking State Before advanceUntilIdle()
```kotlin
// WRONG
viewModel.onEvent(MyEvent.LoadData)
assertEquals(data, viewModel.state.value.data) // Fails!

// CORRECT
viewModel.onEvent(MyEvent.LoadData)
advanceUntilIdle() // Wait for coroutines to complete
assertEquals(data, viewModel.state.value.data)
```

### ❌ Pitfall 3: Exact Error Message Matching
```kotlin
// FRAGILE - breaks if message changes
assertEquals("Invalid email: Must contain exactly one @ symbol", error)

// ROBUST - checks for key content
assertTrue(error != null && error.contains("email"))
assertTrue(error != null && error.contains("@"))
```

### ❌ Pitfall 4: Not Canceling Collection Jobs
```kotlin
// MEMORY LEAK
val job = launch(testDispatcher) {
    viewModel.effect.collect { effects.add(it) }
}
// Test ends, job still running

// CORRECT
val job = launch(testDispatcher) {
    viewModel.effect.collect { effects.add(it) }
}
// ... test logic ...
job.cancel() // Always cancel
```

---

## Testing Checklist

When testing a ViewModel, ensure you cover:

- [ ] Initial state is correct
- [ ] State updates on events
- [ ] Loading state during async operations
- [ ] Error handling (repository failures)
- [ ] Validation logic
- [ ] Effects are emitted correctly
- [ ] Edge cases (empty input, null values)
- [ ] Repository/UseCase is called with correct parameters

---

## Example: Complete Test Suite Structure

```kotlin
class MyViewModelTest {
    // Setup...
    
    // Group 1: State Management
    @Test fun `initial state is correct`() { }
    @Test fun `field change updates state`() { }
    @Test fun `toggle updates boolean state`() { }
    
    // Group 2: Async Operations
    @Test fun `load data success updates state`() { }
    @Test fun `load data failure shows error`() { }
    @Test fun `loading state during operation`() { }
    
    // Group 3: Validation
    @Test fun `empty field shows validation error`() { }
    @Test fun `valid input clears error`() { }
    
    // Group 4: Effects
    @Test fun `success emits navigation effect`() { }
    @Test fun `error emits snackbar effect`() { }
}
```

---

## Quick Reference

**Setup:**
- `StandardTestDispatcher()` for controlling time
- `Dispatchers.setMain(testDispatcher)` in @BeforeTest
- `Dispatchers.resetMain()` in @AfterTest

**Timing:**
- `advanceUntilIdle()` - Run all pending coroutines
- `advanceTimeBy(ms)` - Advance virtual time for debounce

**State Testing:**
- `viewModel.state.value` - Current state snapshot
- Check immediately for synchronous updates
- Call `advanceUntilIdle()` for async updates

**Effect Testing:**
- Collect in `launch(testDispatcher) { }`
- Always `advanceUntilIdle()` before assertions
- Always `job.cancel()` after test

**Assertions:**
- Use `contains()` for error messages (not exact match)
- Check `!= null` before accessing properties
- Verify repository was called: `assertNotNull(repo.lastCalledWith)`
