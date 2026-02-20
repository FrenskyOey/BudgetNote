---
name: resource_management
description: Manages shared UI resources (strings, images, fonts) using the 'Res' object. Use when adding or refactoring resources.
---

# Resource Management Skill

## Purpose
This skill ensures that all resources (strings, images, fonts, raw files) are handled using the **Compose Multiplatform Resources** library (`composeResources`), ensuring compatibility across Android, iOS, Desktop, and Web.

## Applicability
Activate this skill when:
- Adding new UI text or labels.
- Adding icons or images.
- Adding custom fonts.
- Reading raw files (JSON, etc.).
- Refactoring hardcoded strings or platform-specific resource calls (e.g., `R.string`, `UIImage`).

## Detection Protocol

**Before applying this skill, scan the codebase for these patterns:**

1. **Platform-Specific Resources:**
   - Android: `R.string.*`, `R.drawable.*`, `R.font.*`
   - iOS: `UIImage(named:)`, `NSLocalizedString`
   
2. **Hardcoded Strings in UI:**
   - `Text("Hardcoded string")` without `stringResource()`
   - Button labels with direct strings
   
3. **Non-Res Image Loading:**
   - `painterResource(R.drawable.*)`
   - Direct file paths to images

**If any pattern is found → Activate this skill and refactor immediately.**

## Core Principle
**NEVER** use platform-specific resource classes (like Android's `R` class or iOS bundles).
**ALWAYS** use the generated `Res` object from the `composeResources` source set.

---

## 1. Strings

### Definition
Define strings in `composeApp/src/commonMain/composeResources/values/strings.xml`.

```xml
<resources>
    <string name="app_name">My App</string>
    <string name="welcome_message">Welcome, %s!</string>
</resources>
```

### Usage
```kotlin
// ❌ WRONG - Hardcoded or Android specific
Text("Hello World")
Text(stringResource(R.string.hello_world))

// ✅ CORRECT - Multiplatform Resource
import org.jetbrains.compose.resources.stringResource
import [package].generated.resources.Res
import [package].generated.resources.app_name

Text(stringResource(Res.string.app_name))
```

## 2. Images & Icons

### Definition
Place images (PNG, JPG, WEBP, XML Vectors) in `composeApp/src/commonMain/composeResources/drawable/`.

### Usage
```kotlin
// ❌ WRONG
Image(painter = painterResource(R.drawable.logo), ...)

// ✅ CORRECT
import org.jetbrains.compose.resources.painterResource
import [package].generated.resources.Res
import [package].generated.resources.logo

Image(
    painter = painterResource(Res.drawable.logo),
    contentDescription = null
)
```

## 3. Fonts

### Definition
Place font files (TTF, OTF) in `composeApp/src/commonMain/composeResources/font/`.

### Usage
```kotlin
import org.jetbrains.compose.resources.Font
import [package].generated.resources.Res
import [package].generated.resources.roboto_bold

val fontFamily = FontFamily(Font(Res.font.roboto_bold))
```

## 4. Raw Files (Assets)

### Definition
Place arbitrary files in `composeApp/src/commonMain/composeResources/files/`.

### Usage
```kotlin
// Reading text asynchronously
val jsonString = Res.readBytes("files/data.json").decodeToString()
```

---

## Naming Conventions

| Type | Prefix | Format | Example |
| :--- | :--- | :--- | :--- |
| **Strings** | None | `snake_case` | `login_button_title` |
| **Icons** | `ic_` | `snake_case` | `ic_settings.xml` |
| **Images** | `img_` | `snake_case` | `img_background.png` |
| **Fonts** | None | `snake_case` | `roboto_bold.ttf` |

## Verification Checklist
- [ ] Are strings defined in `values/strings.xml`?
- [ ] Are images in `drawable/`?
- [ ] Is `Res.string` / `Res.drawable` used instead of `R.*`?
- [ ] Are imports pointing to the generated `Res` class?

## Post-Implementation Verification

After refactoring resources:
1. **Build Test**: Run `./gradlew build` to ensure Res generation succeeds
2. **Search for Violations**: Use `grep_search` to find any remaining `R.string` or hardcoded strings
3. **Runtime Test**: Launch app and verify all resources load correctly across platforms