---
name: compose-navigation
description: Implements type-safe navigation patterns. Use when adding screens, deep links, or passing arguments.
---

# Compose Navigation

## Overview

Implement type-safe navigation in Jetpack Compose applications using the Navigation Compose library.

## Detection Protocol

**Activate this skill when you detect:**
- User mentions: "add screen", "new page", "navigation", "routing"
- Code needs to navigate between composables
- Deep link configuration required
- Argument passing between screens needed

## Quick Decision Tree

**Choose your section based on the task:**

| Task | Go To |
|------|-------|
| Adding a new screen? | → "Define Routes" + "Create NavHost" |
| Passing data between screens? | → "Argument Handling" |
| Deep link from notification/web? | → See [Navigation Patterns Reference](./references/navigation_patterns_reference.md#deep-links) |
| Bottom navigation bar? | → See [Navigation Patterns Reference](./references/navigation_patterns_reference.md#bottom-navigation-pattern) |
| Nested flows (e.g., auth)? | → See [Navigation Patterns Reference](./references/navigation_patterns_reference.md#nested-navigation) |

---

## Core Concepts

### 1. Define Routes (Type-Safe)

Use `@Serializable` data classes/objects for type-safe routes:

```kotlin
import kotlinx.serialization.Serializable

// Simple screen (no arguments)
@Serializable
object Home

// Screen with required argument
@Serializable
data class Profile(val userId: String)

// Screen with optional argument
@Serializable
data class Settings(val section: String? = null)

// Screen with multiple arguments
@Serializable
data class ProductDetail(val productId: String, val showReviews: Boolean = false)
```

### 2. Create NavController

```kotlin
@Composable
fun MyApp() {
    val navController = rememberNavController()
    
    AppNavHost(navController = navController)
}
```

### 3. Create NavHost

```kotlin
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        composable<Home> {
            HomeScreen(
                onNavigateToProfile = { userId ->
                    navController.navigate(Profile(userId))
                }
            )
        }
        
        composable<Profile> { backStackEntry ->
            val profile: Profile = backStackEntry.toRoute()
            ProfileScreen(userId = profile.userId)
        }
        
        composable<Settings> { backStackEntry ->
            val settings: Settings = backStackEntry.toRoute()
            SettingsScreen(section = settings.section)
        }
    }
}
```

---

## Common Patterns

For detailed patterns and examples, see **[Navigation Patterns Reference](./references/navigation_patterns_reference.md)**

### Basic Navigation

```kotlin
// Navigate forward
navController.navigate(Profile(userId = "user123"))

// Navigate back
navController.popBackStack()
```

### Argument Handling

```kotlin
composable<Profile> { backStackEntry ->
    val profile: Profile = backStackEntry.toRoute()
    ProfileScreen(userId = profile.userId)
}
```

**Important**: Pass only IDs or primitives, not complex objects.

---


## Critical Rules

### DO

- Use `@Serializable` routes for type safety
- Pass only IDs/primitives as arguments
- Use `popUpTo` with `launchSingleTop` for bottom navigation
- Extract `NavHost` to a separate composable for testability
- Use `SavedStateHandle.toRoute<T>()` in ViewModels

### DON'T

- Pass complex objects as navigation arguments
- Create `NavController` inside `NavHost`
- Navigate in `LaunchedEffect` without proper keys
- Forget `FLAG_IMMUTABLE` for PendingIntents (Android 12+)
- Use string-based routes (legacy pattern)

---

## Verification Checklist

Before completing navigation implementation:
- [ ] Routes use `@Serializable` data classes/objects
- [ ] Only IDs or primitives passed as arguments
- [ ] NavHost extracted to separate composable
- [ ] Deep links tested (if applicable)
- [ ] Back stack behavior verified

---

## References

- [Navigation with Compose](https://developer.android.com/develop/ui/compose/navigation)
- [Type-Safe Navigation](https://developer.android.com/guide/navigation/design#compose)
- [Navigation Patterns Reference](./references/navigation_patterns_reference.md) (Detailed examples)