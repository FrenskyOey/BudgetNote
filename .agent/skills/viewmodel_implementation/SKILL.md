---
name: ViewModel Implementation
description: Enforces MVI pattern for ViewModels with State/Intent/Effect, UseCase interaction rules, and UDF compliance. Use when creating or modifying any ViewModel.
---

# ViewModel Implementation Skill

## When to Use
- Creating or modifying any `*ViewModel.kt`
- Adding new intents, state fields, or effects

---

## File Structure

```
feature/<name>/ui/<screen>/
├── <Name>Screen.kt
├── <Name>ViewModel.kt
└── state/
    └── <Name>Contract.kt   ← State + Intent + Effect in one file
```

---

## Contract (`<Name>Contract.kt`)

```kotlin
data class <Name>State(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    // domain data...
)

sealed interface <Name>Intent {
    data object Refresh : <Name>Intent
    data class SomeAction(val param: String) : <Name>Intent
}

sealed interface <Name>Effect {
    data class ShowError(val message: String) : <Name>Effect
    data class NavigateTo(val route: String) : <Name>Effect
}
```

**Naming:** Intent = verbs (`Load`, `Refresh`, `Submit`). Effect = commands (`ShowError`, `NavigateTo`).

---

## ViewModel Structure

```kotlin
class <Name>ViewModel(
    private val getDataUseCase: GetDataUseCase,       // Query → Flow
    private val doActionUseCase: DoActionUseCase      // Command → suspend Result
) : ViewModel() {

    private val _uiState = MutableStateFlow(<Name>State())
    val uiState: StateFlow<<Name>State> = _uiState.asStateFlow()

    private val _effect = Channel<<Name>Effect>()
    val effect: Flow<<Name>Effect> = _effect.receiveAsFlow()

    init { observeData() }  // init: ONLY observers, no commands

    fun onIntent(intent: <Name>Intent) {
        when (intent) {
            is <Name>Intent.Refresh -> refresh()
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            getDataUseCase().collect { data ->
                _uiState.update { it.copy(data = data, isLoading = false) }
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            when (val result = doActionUseCase()) {
                is Result.Success -> _uiState.update { it.copy(isRefreshing = false) }
                is Result.Error -> {
                    _uiState.update { it.copy(isRefreshing = false) }
                    _effect.send(<Name>Effect.ShowError(result.exception.message ?: "Error"))
                }
                is Result.Loading -> { /* handled above */ }
            }
        }
    }
}
```

---

## Rules

### 1. Dependencies: UseCases Only
- ✅ ViewModel depends only on UseCases
- ❌ Never inject a Repository directly
- **Exception**: `SessionRepository` is allowed only in auth-related ViewModels (e.g., `LoginViewModel`) because session is app-wide infrastructure

### 2. UseCase Call Pattern

| UseCase returns | How to call |
|---|---|
| `Flow<T>` (query) | Collect in `observeX()` called from `init` |
| `suspend Result<T>` (command) | Call inside `viewModelScope.launch` |
| `Result<T>` (sync validation) | Call directly, no coroutine |

### 3. UDF — Unidirectional Data Flow

```
UI ──── Intent ────▶ ViewModel ──── State/Effect ────▶ UI
```

- UI sends `Intent` via `onIntent()` — **no logic in click handlers**
- ViewModel is the **only** place that calls `_uiState.update { }`
- State flows **down** as `StateFlow`, Effects as `Channel`
- UI **never** calls UseCases or Repositories directly

### 4. Guard Patterns (prevent race conditions)

```kotlin
private fun loadNextPage() {
    // Skip if already in progress or finished
    if (_uiState.value.isPaginationLoading ||
        _uiState.value.isEndReached ||
        _uiState.value.isRefreshing) return
    // Skip if no data yet — let refresh handle initial load
    if (_uiState.value.articles.isEmpty()) return
    // ...
}
```

### 5. State Safety
- Always use `_uiState.update { it.copy(...) }` — never `_uiState.value = ...`
- Never expose `MutableStateFlow` — always expose as `StateFlow`
- Use `Channel` for effects (not `SharedFlow`) — ensures one-time delivery

---

## Verification Checklist

- [ ] `State`, `Intent`, `Effect` in `state/<Name>Contract.kt`
- [ ] Single `onIntent()` entry point
- [ ] `init` only calls `observe*()` functions
- [ ] ViewModel depends only on UseCases (no Repository, except `SessionRepository`)
- [ ] Flow UseCases collected in `observe*()`, suspend UseCases called in `launch`
- [ ] All state mutations use `_uiState.update { }`
- [ ] `Channel` used for effects
- [ ] Guards in place for concurrent async operations
- [ ] Tests follow `viewmodel_testing` skill
