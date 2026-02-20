---
name: UI Validation
description: Validates UI components against the Core Design System and fixes violations. Use after implementing any UI.
---

# UI Validation & Repair Skill

This skill is designed to **audit and automatically fix** UI code that violates the Core Design System rules.

## When to Use
- **After implementing any UI component or screen.**
- When the user asks to "check UI" or "validate design".
- As a mandatory verification step in the `implement_feature` workflow.

## Validation Protocol

**Scan the target file(s) for the following violations. If found, APPLY THE FIX immediately.**

### 1. Direct MaterialTheme Color Access
**Violation**: accessing `MaterialTheme.colorScheme.primary` (or any other color).
**Fix**: Replace with `core.theme` helper function.

| Violation | Fix |
|-----------|-----|
| `MaterialTheme.colorScheme.primary` | `getPrimaryColor()` |
| `MaterialTheme.colorScheme.onSurface` | `getOnSurfaceColor()` |
| `MaterialTheme.colorScheme.background` | `getBackgroundColor()` |
| *(and so on for all colors)* | *(use corresponding `get*Color()`)* |

### 2. Hardcoded Colors
**Violation**: `Color(0xFF...)`, `Color.Red`, `Color.White`.
**Fix**:
1.  **Identify** the closest semantic color from `core.theme.Color.kt`.
2.  **Replace** with `get*Color()`.
3.  **If no match**: Ask user if a new color token is needed.

### 3. Direct MaterialTheme Typography Access
**Violation**: `MaterialTheme.typography.bodyLarge`.
**Fix**: Replace with `core.theme` helper function.

| Violation | Fix |
|-----------|-----|
| `MaterialTheme.typography.displayLarge` | `getTextDisplayLarge()` |
| `MaterialTheme.typography.headlineMedium` | `getTextHeadlineMedium()` |
| `MaterialTheme.typography.bodyMedium` | `getTextBodyMedium()` |
| `MaterialTheme.typography.labelSmall` | `getTextLabelSmall()` |
| *(and so on for all styles)* | *(use corresponding `getText*()`)* |

### 4. Hardcoded Dimensions
**Violation**: `16.dp`, `24.dp`, `12.sp`.
**Fix**: Replace with `Spacing` or `Dimens` object.

| Violation | Fix |
|-----------|-----|
| `4.dp` | `Spacing.Tiny` |
| `8.dp` | `Spacing.Small` |
| `16.dp` | `Spacing.Medium` |
| `24.dp` | `Spacing.Large` |
| `32.dp` | `Spacing.ExtraLarge` |
| `Icon Size (24.dp)` | `ComponentDimens.IconSizeMedium` |

### 5. Raw Compose Components
**Violation**: Using standard Material3 components instead of Core components.
**Fix**: Replace with `core.components` equivalent.

| Violation | Fix |
|-----------|-----|
| `Button` | `CoreButton` |
| `OutlinedButton` | `CoreOutlinedButton` |
| `TextField` | `CoreTextInput` |
| `OutlinedTextField` | `CoreTextInput` |
| `Checkbox` | `CoreCheckbox` |
| `Switch` | `CoreSwitch` |
| `TopAppBar` | `CoreTopAppBar` |

## Execution Steps

1.  **Read File**: View the content of the UI file.
2.  **Audit**: Check for violations listed above.
3.  **Report**: If violations found, list them briefly.
4.  **Fix**: Apply changes using `replace_file_content`.
5.  **Verify**: Ensure imports are correct:
    - `import core.theme.*`
    - `import core.components.*`
    - Remove unused `androidx.compose.material3.*` imports (unless needed for modifiers/icons).
