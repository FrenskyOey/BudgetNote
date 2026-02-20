package core.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import core.theme.*

/**
 * Standard Text Input Field
 * Material Design 3 outlined text field with label, helper text, and error support
 *
 * @param value Current text value
 * @param onValueChange Callback when text changes
 * @param label Label text shown above the field
 * @param modifier Optional modifier
 * @param placeholder Optional placeholder text
 * @param helperText Optional helper text shown below field
 * @param errorText Optional error message (shows field in error state)
 * @param leadingIcon Optional icon shown at start of field
 * @param trailingIcon Optional icon shown at end of field
 * @param enabled Whether field is enabled
 * @param readOnly Whether field is read-only
 * @param singleLine Whether field is single line
 * @param maxLines Maximum number of lines
 * @param keyboardOptions Keyboard configuration
 * @param keyboardActions Keyboard action handlers
 */
@Composable
fun CoreTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    helperText: String? = null,
    errorText: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
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

        // Text Field
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder?.let {
                { Text(text = it, style = MaterialTheme.typography.bodyLarge) }
            },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(ComponentDimens.IconSizeMedium)
                    )
                }
            },
            trailingIcon = trailingIcon?.let {
                {
                    if (onTrailingIconClick != null) {
                        IconButton(onClick = onTrailingIconClick) {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                modifier = Modifier.size(ComponentDimens.IconSizeMedium)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            modifier = Modifier.size(ComponentDimens.IconSizeMedium)
                        )
                    }
                }
            },
            enabled = enabled,
            readOnly = readOnly,
            isError = isError,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                },
                unfocusedBorderColor = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.outline
                },
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorLeadingIconColor = MaterialTheme.colorScheme.error,
                errorTrailingIconColor = MaterialTheme.colorScheme.error
            ),
            shape = MaterialTheme.shapes.small,
            textStyle = MaterialTheme.typography.bodyLarge,
            visualTransformation = visualTransformation
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
}

/**
 * Username Input Field
 * Pre-configured text field for username input with icon
 */
@Composable
fun CUsernameInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Username",
    helperText: String? = "Must be unique",
    errorText: String? = null,
    enabled: Boolean = true
) {
    CoreTextInput(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        helperText = helperText,
        errorText = errorText,
        leadingIcon = Icons.Default.Person,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )
    )
}

/**
 * Email Input Field
 * Pre-configured text field for email input with validation icon
 */
@Composable
fun CoreEmailInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Email",
    placeholder: String = "example@domain.com",
    helperText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true,
    onClearClick: (() -> Unit)? = null
) {
    CoreTextInput(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        placeholder = placeholder,
        helperText = helperText,
        errorText = errorText,
        leadingIcon = Icons.Default.Email,
        trailingIcon = if (value.isNotEmpty() && onClearClick != null) {
            Icons.Default.Close
        } else null,
        onTrailingIconClick = onClearClick,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
}

/**
 * Password Input Field
 * Text field with show/hide password toggle
 */
@Composable
fun CorePasswordInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    helperText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true,
    isPasswordVisible: Boolean? = null,
    onTogglePasswordVisibility: (() -> Unit)? = null
) {
    val (visible, onToggle) = if (isPasswordVisible != null && onTogglePasswordVisibility != null) {
        isPasswordVisible to onTogglePasswordVisibility
    } else {
        val internalState = remember { mutableStateOf(false) }
        internalState.value to { internalState.value = !internalState.value }
    }

    CoreTextInput(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        helperText = helperText,
        errorText = errorText,
        leadingIcon = Icons.Default.Key,
        trailingIcon = if (visible) {
            Icons.Default.VisibilityOff
        } else {
            Icons.Default.Visibility
        },
        onTrailingIconClick = onToggle,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (visible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}

/**
 * Phone Number Input Field
 * Pre-configured text field for phone number with validation
 */
@Composable
fun CorePhoneInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Phone Number",
    placeholder: String = "+6281xxxxxx",
    helperText: String? = null,
    errorText: String? = "Invalid phone format. Please check again.",
    enabled: Boolean = true
) {
    val isError = errorText != null && value.isNotEmpty()

    CoreTextInput(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        placeholder = placeholder,
        helperText = if (!isError) helperText else null,
        errorText = if (isError) errorText else null,
        trailingIcon = if (isError) Icons.Default.Error else null,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        )
    )
}

/**
 * Search Input Field
 * Text field optimized for search with search icon
 */
@Composable
fun CoreSearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    onSearchClick: (() -> Unit)? = null,
    onClearClick: (() -> Unit)? = null,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(text = placeholder) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(ComponentDimens.IconSizeMedium)
            )
        },
        trailingIcon = if (value.isNotEmpty() && onClearClick != null) {
            {
                IconButton(onClick = onClearClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        modifier = Modifier.size(ComponentDimens.IconSizeMedium)
                    )
                }
            }
        } else null,
        enabled = enabled,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchClick?.invoke() }
        ),
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors()
    )
}

/**
 * Multiline Text Input (Text Area)
 * Text field for longer text input
 */
@Composable
fun CoreTextAreaInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    helperText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true,
    minLines: Int = 4,
    maxLines: Int = 8,
    maxCharacters: Int? = null
) {
    val isError = errorText != null
    val characterCount = value.length
    val showCharacterCount = maxCharacters != null

    Column(modifier = modifier) {
        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (isError) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(bottom = Spacing.Tiny)
        )

        // Text Field
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                if (maxCharacters == null || newValue.length <= maxCharacters) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder?.let {
                { Text(text = it, style = MaterialTheme.typography.bodyLarge) }
            },
            enabled = enabled,
            isError = isError,
            singleLine = false,
            minLines = minLines,
            maxLines = maxLines,
            shape = MaterialTheme.shapes.small,
            colors = OutlinedTextFieldDefaults.colors(),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        // Helper/Error Text and Character Count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Spacing.Medium,
                    top = Spacing.Tiny
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
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
                    modifier = Modifier.weight(1f)
                )
            }

            if (showCharacterCount) {
                Text(
                    text = "$characterCount/${maxCharacters}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (characterCount > maxCharacters!!) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}


