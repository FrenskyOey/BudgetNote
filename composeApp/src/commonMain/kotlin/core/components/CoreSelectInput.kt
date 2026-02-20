package core.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import core.theme.*

/**
 * Checkbox with Label
 * Material Design 3 checkbox with accompanying text
 *
 * @param checked Whether checkbox is checked
 * @param onCheckedChange Callback when checked state changes
 * @param label Label text displayed next to checkbox
 * @param modifier Optional modifier
 * @param enabled Whether checkbox is enabled
 * @param helperText Optional helper text shown below checkbox
 */

@Composable
fun CoreCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    helperText: String? = null
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = checked,
                    onClick = { onCheckedChange(!checked) },
                    enabled = enabled,
                    role = Role.Checkbox
                )
                .padding(vertical = Spacing.ExtraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = null, // Handled by Row's onClick
                enabled = enabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                )
            )

            Spacer(modifier = Modifier.width(Spacing.Small))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                }
            )
        }

        // Helper Text
        if (helperText != null) {
            Text(
                text = helperText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(
                    start = Dimens.XXXL, // Align with text after checkbox
                    top = Spacing.Tiny
                )
            )
        }
    }
}

/**
 * Checkbox Group
 * Multiple checkboxes grouped together with a title
 *
 * @param title Group title/label
 * @param options List of checkbox options with their checked states
 * @param onOptionChange Callback when an option is toggled
 * @param modifier Optional modifier
 * @param enabled Whether all checkboxes are enabled
 */
@Composable
fun CoreCheckboxGroup(
    title: String,
    options: List<CheckboxOption>,
    onOptionChange: (CheckboxOption, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        // Title
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = Spacing.Tiny)
        )

        // Checkboxes
        options.forEach { option ->
            CoreCheckbox(
                checked = option.checked,
                onCheckedChange = { newChecked ->
                    onOptionChange(option, newChecked)
                },
                label = option.label,
                enabled = enabled && option.enabled
            )
        }
    }
}

/**
 * Data class for checkbox options
 */
data class CheckboxOption(
    val id: String,
    val label: String,
    val checked: Boolean,
    val enabled: Boolean = true
)

/**
 * Radio Button with Label
 * Material Design 3 radio button with accompanying text
 *
 * @param selected Whether radio button is selected
 * @param onClick Callback when radio button is clicked
 * @param label Label text displayed next to radio button
 * @param modifier Optional modifier
 * @param enabled Whether radio button is enabled
 */
@Composable
fun CoreRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.RadioButton
            )
            .padding(vertical = Spacing.ExtraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // Handled by Row's onClick
            enabled = enabled,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.width(Spacing.Small))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (enabled) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            }
        )
    }
}

/**
 * Radio Button Group
 * Multiple radio buttons for single selection
 *
 * @param title Group title/label
 * @param options List of radio button options
 * @param selectedOption Currently selected option
 * @param onOptionSelected Callback when an option is selected
 * @param modifier Optional modifier
 * @param enabled Whether all radio buttons are enabled
 * @param optionLabel Function to get display label for option
 */
@Composable
fun <T> CoreRadioButtonGroup(
    title: String,
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    optionLabel: (T) -> String = { it.toString() }
) {
    Column(
        modifier = modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        // Title
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = Spacing.Tiny)
        )

        // Radio Buttons
        options.forEach { option ->
            CoreRadioButton(
                selected = option == selectedOption,
                onClick = { onOptionSelected(option) },
                label = optionLabel(option),
                enabled = enabled
            )
        }
    }
}

/**
 * Switch with Label
 * Material Design 3 switch (toggle) with accompanying text
 *
 * @param checked Whether switch is checked (on)
 * @param onCheckedChange Callback when checked state changes
 * @param label Label text displayed next to switch
 * @param modifier Optional modifier
 * @param enabled Whether switch is enabled
 * @param helperText Optional helper text shown below switch
 */
@Composable
fun CoreSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    helperText: String? = null,
    labelPosition: LabelPosition = LabelPosition.Start
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = checked,
                    onClick = { onCheckedChange(!checked) },
                    enabled = enabled,
                    role = Role.Switch
                )
                .padding(vertical = Spacing.ExtraSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (labelPosition == LabelPosition.Start) {
                Arrangement.SpaceBetween
            } else {
                Arrangement.Start
            }
        ) {
            if (labelPosition == LabelPosition.End) {
                Switch(
                    checked = checked,
                    onCheckedChange = null, // Handled by Row's onClick
                    enabled = enabled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
                Spacer(modifier = Modifier.width(Spacing.Small))
            }

            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                },
                modifier = if (labelPosition == LabelPosition.Start) {
                    Modifier.weight(1f)
                } else {
                    Modifier
                }
            )

            if (labelPosition == LabelPosition.Start) {
                Switch(
                    checked = checked,
                    onCheckedChange = null, // Handled by Row's onClick
                    enabled = enabled,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        }

        // Helper Text
        if (helperText != null) {
            Text(
                text = helperText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(
                    start = if (labelPosition == LabelPosition.Start) Spacing.Small else Dimens.XXXL,
                    top = Spacing.Tiny
                )
            )
        }
    }
}

/**
 * Label position for switch
 */
enum class LabelPosition {
    Start,  // Label on the left, switch on the right
    End     // Switch on the left, label on the right
}

/**
 * Switch Group
 * Multiple switches grouped together with a title
 */
@Composable
fun CoreSwitchGroup(
    title: String,
    options: List<SwitchOption>,
    onOptionChange: (SwitchOption, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        // Title
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = Spacing.Tiny)
        )

        // Switches
        options.forEach { option ->
            CoreSwitch(
                checked = option.checked,
                onCheckedChange = { newChecked ->
                    onOptionChange(option, newChecked)
                },
                label = option.label,
                enabled = enabled && option.enabled,
                helperText = option.helperText
            )
        }
    }
}

/**
 * Data class for switch options
 */
data class SwitchOption(
    val id: String,
    val label: String,
    val checked: Boolean,
    val enabled: Boolean = true,
    val helperText: String? = null
)

/**
 * Tri-State Checkbox
 * Checkbox with three states: checked, unchecked, indeterminate
 * Useful for "Select All" functionality
 */
@Composable
fun CoreStateCheckbox(
    state: ToggleableState,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = state == ToggleableState.On,
                onClick = onClick,
                enabled = enabled,
                role = Role.Checkbox
            )
            .padding(vertical = Spacing.ExtraSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TriStateCheckbox(
            state = state,
            onClick = null, // Handled by Row's onClick
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                checkmarkColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        Spacer(modifier = Modifier.width(Spacing.Small))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (enabled) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            }
        )
    }
}