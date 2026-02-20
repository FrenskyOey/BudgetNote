# Design System Reference

> **Note**: This is a reference document for the UI Implementation skill. It contains all design tokens and component specifications.

---

## Color Helpers

| Function | Description |
|----------|-------------|
| `getPrimaryColor()` | Primary brand color |
| `getOnPrimaryColor()` | Content on primary |
| `getPrimaryContainerColor()` | Primary container |
| `getOnPrimaryContainerColor()` | Content on primary container |
| `getSecondaryColor()` | Secondary color |
| `getOnSecondaryColor()` | Content on secondary |
| `getSecondaryContainerColor()` | Secondary container |
| `getOnSecondaryContainerColor()` | Content on secondary container |
| `getTertiaryColor()` | Tertiary color |
| `getTertiaryContainerColor()` | Tertiary container |
| `getBackgroundColor()` | Background |
| `getOnBackgroundColor()` | Content on background |
| `getSurfaceColor()` | Surface |
| `getOnSurfaceColor()` | Content on surface |
| `getSurfaceVariantColor()` | Surface variant |
| `getOutlineColor()` | Outline/border |
| `getErrorColor()` | Error state |
| `getOnErrorColor()` | Content on error |
| `getErrorContainerColor()` | Error container |

---

## Spacing Values

| Object | Value | Size |
|--------|-------|------|
| `Dimens.XXS` | 2dp | Extra extra small |
| `Dimens.XS` | 4dp | Extra small |
| `Dimens.S` | 8dp | Small |
| `Dimens.SM` | 12dp | Small-Medium |
| `Dimens.M` | 16dp | Medium (base) |
| `Dimens.ML` | 20dp | Medium-Large |
| `Dimens.L` | 24dp | Large |
| `Dimens.XL` | 32dp | Extra large |
| `Dimens.XXL` | 40dp | Extra extra large |
| `Dimens.XXXL` | 48dp | Extra extra extra large |
| `Dimens.H` | 56dp | Huge |
| `Dimens.XH` | 64dp | Extra huge |
| `Dimens.XXH` | 72dp | Extra extra huge |
| `Dimens.XXXH` | 80dp | Maximum |

### Semantic Spacing

| Semantic | Maps To | Size |
|----------|---------|------|
| `Spacing.Micro` | Dimens.XXS | 2dp |
| `Spacing.Tiny` | Dimens.XS | 4dp |
| `Spacing.ExtraSmall` | Dimens.S | 8dp |
| `Spacing.Small` | Dimens.SM | 12dp |
| `Spacing.Medium` | Dimens.M | 16dp |
| `Spacing.MediumPlus` | Dimens.ML | 20dp |
| `Spacing.Large` | Dimens.L | 24dp |
| `Spacing.ExtraLarge` | Dimens.XL | 32dp |
| `Spacing.Huge` | Dimens.XXL | 40dp |
| `Spacing.Massive` | Dimens.XXXL | 48dp |
| `Spacing.Giant` | Dimens.H | 56dp |
| `Spacing.Enormous` | Dimens.XH | 64dp |
| `Spacing.Colossal` | Dimens.XXH | 72dp |
| `Spacing.Maximum` | Dimens.XXXH | 80dp |

---

## Component Dimensions

| Value | Size | Use For |
|-------|------|---------|
| `ComponentDimens.ButtonHeightSmall` | 32dp | Small buttons |
| `ComponentDimens.ButtonHeightMedium` | 40dp | Default buttons |
| `ComponentDimens.ButtonHeightLarge` | 48dp | Large buttons |
| `ComponentDimens.IconSizeSmall` | 16dp | Small icons |
| `ComponentDimens.IconSizeMedium` | 24dp | Default icons |
| `ComponentDimens.IconSizeLarge` | 32dp | Large icons |
| `ComponentDimens.CardCornerRadius` | 12dp | Card corners |
| `ComponentDimens.TextFieldHeight` | 56dp | Input height |
| `ComponentDimens.TopAppBarHeight` | 64dp | Top bar height |

---

## Typography Helpers

| Function | Size | Weight |
|----------|------|--------|
| `getTextDisplayLarge()` | 57sp | Normal |
| `getTextDisplayMedium()` | 45sp | Normal |
| `getTextDisplaySmall()` | 36sp | Normal |
| `getTextHeadlineLarge()` | 32sp | Normal |
| `getTextHeadlineMedium()` | 28sp | Normal |
| `getTextHeadlineSmall()` | 24sp | Normal |
| `getTextTitleLarge()` | 22sp | Normal |
| `getTextTitleMedium()` | 16sp | Medium |
| `getTextTitleSmall()` | 14sp | Medium |
| `getTextBodyLarge()` | 16sp | Normal |
| `getTextBodyMedium()` | 14sp | Normal |
| `getTextBodySmall()` | 12sp | Normal |
| `getTextLabelLarge()` | 14sp | Medium |
| `getTextLabelMedium()` | 12sp | Medium |
| `getTextLabelSmall()` | 11sp | Medium |

---

## Available Components

### Buttons (`CoreButton.kt`)
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

### Navigation (`CoreNavBar.kt`)
| Component | Use For |
|-----------|---------|
| `CoreTopAppBar` | Customizable top app bar |
| `CoreBasicAppBar` | Simple app bar with title |
| `CoreBackStackAppBar` | App bar with back button |
| `CoreSearchTopAppBar` | App bar with search action |
| `CoreContentTopAppBar` | App bar with custom content |
| `CoreAppBarAction` | Action icon in app bar |
| `CoreTopBarColor()` | Default app bar colors |

### Pickers (`CorePickers.kt`)
| Component | Use For |
|-----------|---------|
| `CoreDatePickerInput` | Date selection field |
| `CoreTimePickerInput` | Time selection field |
| `CoreDropdownInput` | Generic dropdown/spinner |
| `CoreSpinnerInput` | String-based dropdown |

### Selection Controls (`CoreSelectInput.kt`)
| Component | Use For |
|-----------|---------|
| `CoreCheckbox` | Single checkbox with label |
| `CoreCheckboxGroup` | Multiple checkboxes with title |
| `CoreRadioButton` | Single radio button |
| `CoreRadioButtonGroup` | Radio button group |
| `CoreSwitch` | Toggle switch with label |
| `CoreSwitchGroup` | Multiple switches |
| `CoreStateCheckbox` | Tri-state checkbox |

### Sliders (`CoreSlider.kt`)
| Component | Use For |
|-----------|---------|
| `CoreSlider` | Basic slider |
| `CoreLabelSlider` | Slider with label and value |
| `CorePercentSlider` | 0-100% slider |
| `CoreRangeSlider` | Discrete step slider |
| `CoreSliderMinMax` | Slider with min/max labels |

### Text Inputs (`CoreTextInput.kt`)
| Component | Use For |
|-----------|---------|
| `CoreTextInput` | Standard text field |
| `CUsernameInput` | Username input |
| `CoreEmailInput` | Email input |
| `CorePasswordInput` | Password with visibility toggle |
| `CorePhoneInput` | Phone number input |
| `CoreSearchInput` | Search field |
| `CoreTextAreaInput` | Multiline text area |
