package core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import core.theme.*
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

/**
 * Date Picker Input Field
 * Displays a text field that opens a date picker dialog when clicked
 *
 * @param selectedDate Currently selected date (timestamp in milliseconds)
 * @param onDateSelected Callback when date is selected
 * @param label Label text
 * @param modifier Optional modifier
 * @param dateFormat Format for displaying the date (unused in this KMP implementation, defaults to MMM dd, yyyy)
 * @param helperText Optional helper text
 * @param errorText Optional error message
 * @param enabled Whether the picker is enabled
 */
@OptIn(ExperimentalMaterial3Api::class, kotlin.time.ExperimentalTime::class)
@Composable
fun CoreDatePickerInput(
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    dateFormat: String = "MMM dd, yyyy",
    helperText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true
) {
    var showDialog by remember { mutableStateOf(false) }

    val displayText = selectedDate?.let {
        val instant = Instant.fromEpochMilliseconds(it)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        // Manual formatting for "MMM dd, yyyy" to avoid platform specific SimpleDateFormat
        val monthName = localDate.month.name.take(3).lowercase().replaceFirstChar { char -> char.uppercase() }
        "$monthName ${localDate.dayOfMonth}, ${localDate.year}"
    } ?: ""

    val isError = errorText != null

    Column(modifier = modifier) {
        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = when {
                isError -> MaterialTheme.colorScheme.error
                !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                else -> MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(bottom = Spacing.Tiny)
        )

        // Date Field (clickable)
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { showDialog = true },
            enabled = false,
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Select date",
                    modifier = Modifier.size(ComponentDimens.IconSizeMedium),
                    tint = if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            },
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.outline
                },
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = MaterialTheme.shapes.small,
            textStyle = MaterialTheme.typography.bodyLarge
        )

        // Helper or Error Text
        val supportingText = errorText ?: helperText
        if (supportingText != null) {
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.padding(
                    start = Spacing.Medium,
                    top = Spacing.Tiny
                )
            )
        }
    }

    // Date Picker Dialog
    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate ?: core.util.getCurrentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let(onDateSelected)
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

/**
 * Time Picker Input Field
 * Displays a text field that opens a time picker dialog when clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoreTimePickerInput(
    selectedHour: Int?,
    selectedMinute: Int?,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    is24Hour: Boolean = false,
    helperText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true
) {
    var showDialog by remember { mutableStateOf(false) }

    val displayText = if (selectedHour != null && selectedMinute != null) {
        if (is24Hour) {
            "${selectedHour.toString().padStart(2, '0')}:${selectedMinute.toString().padStart(2, '0')}"
        } else {
            val hour12 = if (selectedHour == 0) 12 else if (selectedHour > 12) selectedHour - 12 else selectedHour
            val amPm = if (selectedHour < 12) "AM" else "PM"
            "${hour12.toString().padStart(2, '0')}:${selectedMinute.toString().padStart(2, '0')} $amPm"
        }
    } else ""

    val isError = errorText != null

    Column(modifier = modifier) {
        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = when {
                isError -> MaterialTheme.colorScheme.error
                !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                else -> MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(bottom = Spacing.Tiny)
        )

        // Time Field (clickable)
        OutlinedTextField(
            value = displayText,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { showDialog = true },
            enabled = false,
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Select time",
                    modifier = Modifier.size(ComponentDimens.IconSizeMedium)
                )
            },
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.outline
                },
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = MaterialTheme.shapes.small,
            textStyle = MaterialTheme.typography.bodyLarge
        )

        // Helper or Error Text
        val supportingText = errorText ?: helperText
        if (supportingText != null) {
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.padding(
                    start = Spacing.Medium,
                    top = Spacing.Tiny
                )
            )
        }
    }

    // Time Picker Dialog
    if (showDialog) {
        val timePickerState = rememberTimePickerState(
            initialHour = selectedHour ?: 0,
            initialMinute = selectedMinute ?: 0,
            is24Hour = is24Hour
        )

        CoreTimePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTimeSelected(timePickerState.hour, timePickerState.minute)
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

/**
 * Time Picker Dialog Wrapper
 */
@Composable
private fun CoreTimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        text = content
    )
}

/**
 * Dropdown/Spinner Input Field
 * Material Design 3 exposed dropdown menu
 *
 * @param options List of selectable options
 * @param selectedOption Currently selected option
 * @param onOptionSelected Callback when option is selected
 * @param label Label text
 * @param modifier Optional modifier
 * @param optionLabel Function to convert option to display text
 * @param helperText Optional helper text
 * @param errorText Optional error message
 * @param enabled Whether the dropdown is enabled
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CoreDropdownInput(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    optionLabel: (T) -> String = { it.toString() },
    helperText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val displayText = selectedOption?.let(optionLabel) ?: ""
    val isError = errorText != null

    Column(modifier = modifier) {
        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = when {
                isError -> MaterialTheme.colorScheme.error
                !enabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                else -> MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(bottom = Spacing.Tiny)
        )

        // Dropdown Field
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = !expanded }
        ) {
            OutlinedTextField(
                value = displayText,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                enabled = enabled,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                isError = isError,
                colors = OutlinedTextFieldDefaults.colors(),
                shape = MaterialTheme.shapes.small,
                textStyle = MaterialTheme.typography.bodyLarge
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = optionLabel(option),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        // Helper or Error Text
        val supportingText = errorText ?: helperText
        if (supportingText != null) {
            Text(
                text = supportingText,
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.padding(
                    start = Spacing.Medium,
                    top = Spacing.Tiny
                )
            )
        }
    }
}

/**
 * Simple Spinner/Dropdown (alternative implementation)
 * Use this for simple string-based dropdowns
 */
@Composable
fun CoreSpinnerInput(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    helperText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true
) {
    CoreDropdownInput(
        options = options,
        selectedOption = selectedOption,
        onOptionSelected = onOptionSelected,
        label = label,
        modifier = modifier,
        helperText = helperText,
        errorText = errorText,
        enabled = enabled
    )
}