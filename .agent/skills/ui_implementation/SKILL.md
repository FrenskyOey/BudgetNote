---
name: UI Implementation
description: Enforces the Core Design System and forbids hardcoded values. Use when implementing UI layouts or components.
---

# UI Implementation Skill

This skill ensures consistent UI implementation by mandating the use of the project's established design system and component library.

## When This Skill Applies

Automatically apply this skill when:
- Creating new screen layouts
- Adding or modifying UI components
- Working with colors, typography, or spacing
- User requests involving: "layout", "screen", "component", "button", "input", "form", "UI", "design"

## Detection Protocol

**Automatically scan for these violations before implementing UI:**

1. **Hardcoded Colors:**
   - `Color(0xFF...)` or `Color.Red`, `Color.Blue`, etc.
   - `MaterialTheme.colorScheme.primary` (direct access)

2. **Hardcoded Spacing:**
   - `.padding(16.dp)`, `.size(24.dp)`
   - Any numeric `.dp` values

3. **Hardcoded Typography:**
   - `fontSize = 14.sp`, `fontWeight = FontWeight.Bold`
   - `MaterialTheme.typography.*` (direct access)

4. **Missing Core Components:**
   - `Button()` instead of `CoreButton()`
   - `TextField()` instead of `CoreTextInput()`
   - `TopAppBar()` instead of `CoreTopAppBar()`

**If any pattern is found → Activate this skill and refactor to design system.**

---

## Theme System Rules

### Colors

**ALWAYS use theme helper functions from `core.theme.Color`:**

```kotlin
// ✅ CORRECT
import core.theme.*

Text(color = getPrimaryColor())
Box(modifier = Modifier.background(getSurfaceColor()))
Icon(tint = getOnBackgroundColor())

// ❌ WRONG - Hardcoded colors
Text(color = Color(0xFF138AFC))
Box(modifier = Modifier.background(Color.White))

// ❌ WRONG - Direct MaterialTheme access
Text(color = MaterialTheme.colorScheme.primary)
```

### Spacing

**ALWAYS use `Spacing` or `Dimens` objects from `core.theme.Dimens`:**

```kotlin
// ✅ CORRECT
import core.theme.*

Modifier.padding(Spacing.Medium)           // 16dp
Modifier.padding(horizontal = Dimens.L)    // 24dp
Spacer(modifier = Modifier.height(Spacing.Small))  // 12dp
Modifier.size(ComponentDimens.IconSizeMedium)      // 24dp

// ❌ WRONG - Hardcoded dp values
Modifier.padding(16.dp)
Spacer(modifier = Modifier.height(12.dp))
```

### Typography

**ALWAYS use typography helper functions from `core.theme.Typography`:**

```kotlin
// ✅ CORRECT
import core.theme.*

Text(text = "Title", style = getTextTitleLarge())
Text(text = "Body", style = getTextBodyMedium())
Text(text = "Label", style = getTextLabelSmall())

// ❌ WRONG - Hardcoded text styles
Text(text = "Title", fontSize = 22.sp, fontWeight = FontWeight.Bold)

// ❌ WRONG - Direct MaterialTheme access
Text(text = "Title", style = MaterialTheme.typography.titleLarge)
```

---

## Component Library Rules

**ALWAYS check `core/components/` before creating new UI elements.**

### Available Components

#### Buttons (`CoreButton.kt`)
| Component | Use For |
|-----------|---------|
| `CoreButton` | Primary action button |
| `CoreTonalButton` | Secondary filled button |
| `CoreOutlinedButton` | Medium emphasis action |
| `CoreTextButton` | Low emphasis action |
| `CoreIconButton` | Icon-only button |
| `CoreFilledIconButton` | Icon button with filled background |
| `CoreTonalIconButton` | Icon button with tonal background |
| `CoreOutlinedIconButton` | Icon button with border |
| `CoreSmallFloatingActionButton` | Small FAB |
| `CoreFloatingActionButton` | Regular FAB |
| `CoreExtendedFloatingActionButton` | FAB with icon and text |
| `CoreSegmentedButtonGroup` | Segmented button selection |

#### Navigation (`CoreNavBar.kt`)
| Component | Use For |
|-----------|---------|
| `CoreTopAppBar` | Customizable top app bar |
| `CoreBasicAppBar` | Simple app bar with title |
| `CoreBackStackAppBar` | App bar with back button |
| `CoreSearchTopAppBar` | App bar with search action |
| `CoreContentTopAppBar` | App bar with custom content |
| `CoreAppBarAction` | Action icon in app bar |
| `CoreTopBarColor()` | Default app bar colors |

#### Pickers (`CorePickers.kt`)
| Component | Use For |
|-----------|---------|
| `CoreDatePickerInput` | Date selection field |
| `CoreTimePickerInput` | Time selection field |
| `CoreDropdownInput` | Generic dropdown/spinner |
| `CoreSpinnerInput` | String-based dropdown |

#### Selection Controls (`CoreSelectInput.kt`)
| Component | Use For |
|-----------|---------|
| `CoreCheckbox` | Single checkbox with label |
| `CoreCheckboxGroup` | Multiple checkboxes with title |
| `CoreRadioButton` | Single radio button |
| `CoreRadioButtonGroup` | Radio button group |
| `CoreSwitch` | Toggle switch with label |
| `CoreSwitchGroup` | Multiple switches |
| `CoreStateCheckbox` | Tri-state checkbox |

#### Sliders (`CoreSlider.kt`)
| Component | Use For |
|-----------|---------|
| `CoreSlider` | Basic slider |
| `CoreLabelSlider` | Slider with label and value |
| `CorePercentSlider` | 0-100% slider |
| `CoreRangeSlider` | Discrete step slider |
| `CoreSliderMinMax` | Slider with min/max labels |

#### Text Inputs (`CoreTextInput.kt`)
| Component | Use For |
|-----------|---------|
| `CoreTextInput` | Standard text field |
| `CUsernameInput` | Username input |
| `CoreEmailInput` | Email input |
| `CorePasswordInput` | Password with visibility toggle |
| `CorePhoneInput` | Phone number input |
| `CoreSearchInput` | Search field |
| `CoreTextAreaInput` | Multiline text area |

---

## Self-Updating Design System

When you encounter a design requirement that doesn't exist in the current design system, **YOU MUST ASK THE USER FIRST** before proceeding.

### New Color Needed

```
"The design requires a '[color name]' color that doesn't exist in Color.kt.
Would you like me to:
(A) Add a new color to the design system (Light/Dark variants + helper function)?
(B) Use an existing color instead? If so, which one?"
```

**If user says ADD:**
1. Add `Light[Name]` and `Dark[Name]` to `Color.kt`
2. Add to `LightColorScheme` and `DarkColorScheme`
3. Add helper function `get[Name]Color()`
4. Update this SKILL.md reference table

### New Component Needed

```
"The design requires a '[component name]' component that doesn't exist in core/components/.
Would you like me to:
(A) Create a new Core[Name] component in core/components/?
(B) Implement it inline in this feature only?
(C) Use an existing component as alternative?"
```

**If user says CREATE:**
1. Create `Core[Name].kt` in `core/components/`
2. Follow existing component patterns (use theme values)
3. Update this SKILL.md component table

### New Typography Style Needed

```
"The design requires a '[style name]' text style that doesn't exist.
Would you like me to:
(A) Add a new typography style to the design system?
(B) Use existing style with .copy() modifier?
(C) Use an existing style as-is?"
```

### New Spacing Value Needed

```
"The design requires [X]dp spacing not in Dimens.kt.
Would you like me to:
(A) Add a new spacing value?
(B) Use closest existing value? ([closest options])"
```




---

## Component File Organization (MANDATORY)

### Rule: One Component Per File

**Reusable components MUST be in separate files in the `components/` folder.**

**✅ CORRECT Structure**:
```kotlin
feature/news/ui/detail/
├── NewsDetailScreen.kt          // Main screen composable only
├── NewsDetailViewModel.kt
└── components/
    ├── NewsDetailHeader.kt      // @Composable fun NewsDetailHeader(...)
    ├── NewsAuthor.kt             // @Composable fun NewsAuthor(...)
    ├── NewsDetailTags.kt         // @Composable fun NewsDetailTags(...)
    └── NewsContentItem.kt        // @Composable fun NewsContentItem(...)
```

**❌ WRONG - Everything in one file**:
```kotlin
// NewsDetailScreen.kt
@Composable fun NewsDetailScreen() { ... }
@Composable fun NewsDetailHeader() { ... }   // ❌ Should be components/NewsDetailHeader.kt
@Composable fun NewsAuthor() { ... }         // ❌ Should be components/NewsAuthor.kt
@Composable fun NewsDetailTags() { ... }     // ❌ Should be components/NewsDetailTags.kt
```

### When to Separate

**Create separate component file if**:
- Component is reusable (used >1 time)
- Component is >50 lines
- Component has complex logic
- Component defined in `ui-spec.md`

**Can stay inline in Screen file if**:
- One-time use AND <30 lines
- Simple wrapper (Box, Column with basic styling)

### Implementation Protocol

**During Phase 4.3 (Component Implementation)**:

1. **Before writing any `@Composable`**:
   - Check: Is this in the wireframe/ui-spec?
   - If YES → Create `components/[ComponentName].kt`
   - Announce: "Creating: `components/[ComponentName].kt`"

2. **File Structure**:
```kotlin
// components/NewsDetailHeader.kt
package feature.news.ui.detail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import core.theme.*

@Composable
fun NewsDetailHeader(
    imageUrl: String,
    category: String,
    modifier: Modifier = Modifier
) {
    // Implementation using design system
}
```

3. **Never ask permission** for this organization - it's mandatory

### Auto-Enforcement

**If you detect**:
```kotlin
// In NewsDetailScreen.kt
@Composable fun NewsDetailHeader() { ... }  // >50 lines
```

**Action**:
1. Stop implementation
2. State: "Moving `NewsDetailHeader` to separate file (follows component organization rules)"
3. Create `components/NewsDetailHeader.kt`
4. Continue

---


## Quick Reference

For complete design system reference (colors, spacing, typography, components), see:
**[Design System Reference](./references/design_system_reference.md)**

---

## Verification Checklist

Before completing any UI implementation, verify:

- [ ] All colors use theme helper functions (`get*Color()`)
- [ ] All spacing uses `Spacing.*` or `Dimens.*` objects
- [ ] All typography uses theme helpers (`getText*()`)
- [ ] All buttons use `Core*Button` variants
- [ ] All text inputs use `Core*Input` variants
- [ ] All app bars use `Core*AppBar` variants
- [ ] All selection controls use `Core*` variants
- [ ] No hardcoded dp, sp, or Color values
- [ ] Proper imports from `core.theme.*` and `core.components.*`
- [ ] Components separated into `components/` folder (one per file)
- [ ] Screen file contains only main screen composable
- [ ] Asked user before adding any new design system elements
- [ ] Updated this SKILL.md if new elements were added

---

<!-- LAST_UPDATED: 2026-02-05 -->
