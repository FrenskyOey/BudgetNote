---
description: UI organization and navigation rules
---

# KMP UI Organization

## Feature-Based UI Structure

> [!NOTE]
> Please refer to `project_architecture.md` for the canonical directory structure.

---

## UI Rules

```kotlin
// ✅ CORRECT - ViewModel uses use cases from its own feature only
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val sessionRepository: SessionRepository  // OK — SessionRepository is in core/
) : ViewModel()

// ❌ WRONG - ViewModel pulls from a different feature
class LoginViewModel(
    private val getSettingsUseCase: GetSettingsUseCase  // WRONG — settings feature!
) : ViewModel()
```

---

## Route Definitions

All routes are defined as a **`@Serializable` sealed class** in `com.app.budgetnote.navigation.AppRoutes.kt`.

```kotlin
// com/app/budgetnote/navigation/AppRoutes.kt
@Serializable
sealed class AppRoute {
    @Serializable data object Login : AppRoute()
    @Serializable data object Dashboard : AppRoute()
    
    // Sub-routes are nested inside their feature group
    @Serializable data object SettingsColor : AppRoute()
    @Serializable data object SettingsText : AppRoute()
    @Serializable data object SettingsButton : AppRoute()
    @Serializable data object SettingsForm : AppRoute()
    @Serializable data object SettingsNavbar : AppRoute()
}
```

**Rules:**
- Always add new routes to `AppRoute` — never create ad-hoc route strings.
- Use `data object` for routes with no arguments.
- Use `data class` for routes that carry arguments (e.g., `data class BudgetDetail(val id: String) : AppRoute()`).

---

## NavHost Setup (`App.kt`)

The single `NavHost` lives in `App.kt`. Features expose their screens but **do not own the NavController** — navigation is driven by callbacks passed from `App.kt`.

```kotlin
NavHost(navController = navController, startDestination = AppRoute.Login) {
    composable<AppRoute.Login> {
        LoginScreen()
    }
    composable<AppRoute.Dashboard> {
        DashboardScreen(
            onSettingsAction = { action ->
                when (action) {
                    SettingsAction.OpenColor -> navController.navigate(AppRoute.SettingsColor)
                    SettingsAction.Logout -> coroutineScope.launch { sessionRepository.invalidateSession() }
                }
            }
        )
    }
    composable<AppRoute.SettingsColor> {
        ColorScreen(onBackClick = { navController.popBackStack() })
    }
}
```

---

## Session-Driven Navigation

Navigation between auth and main content is driven by **`SessionRepository.sessionState`** — a `StateFlow<SessionState>` observed in `App.kt`. Screens **never navigate manually** after login/logout.

```kotlin
// core/domain/repository/SessionRepository.kt
sealed class SessionState {
    data object Valid : SessionState()
    data object Invalid : SessionState()
}

interface SessionRepository {
    val sessionState: StateFlow<SessionState>
    suspend fun startSession()
    suspend fun invalidateSession()
}
```

```kotlin
// App.kt — observes session and navigates automatically
val sessionState by sessionRepository.sessionState.collectAsState()

LaunchedEffect(sessionState) {
    when (sessionState) {
        is SessionState.Valid -> navController.navigate(AppRoute.Dashboard) {
            popUpTo(0) { inclusive = true }    // Clear back stack
        }
        is SessionState.Invalid -> navController.navigate(AppRoute.Login) {
            popUpTo(0) { inclusive = true }
        }
    }
}
```

> [!IMPORTANT]
> After a successful login, call `sessionRepository.startSession()` — **do NOT call `navController.navigate()`** directly from a ViewModel or screen. The session observer in `App.kt` handles the navigation automatically.

---

## Adding a New Screen

1. Add a new `data object` (or `data class`) to `AppRoute` in `AppRoutes.kt`
2. Add a `composable<AppRoute.NewScreen>` entry in `NavHost` inside `App.kt`
3. Pass navigation callbacks as lambdas from `App.kt` into the screen composable — screens must **not** hold a reference to `NavController`
