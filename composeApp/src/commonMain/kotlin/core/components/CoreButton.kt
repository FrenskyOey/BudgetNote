package core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import core.theme.ComponentDimens
import core.theme.Spacing

@Composable
fun CoreButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(ComponentDimens.ButtonHeightMedium),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(
            horizontal = ComponentDimens.ButtonPaddingHorizontal,
            vertical = ComponentDimens.ButtonPaddingVertical
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(ComponentDimens.IconSizeMedium)
            )
            Spacer(modifier = Modifier.width(Spacing.ExtraSmall))
        }
        Text(text = text)
    }
}

/**
 * CButton with custom content
 * Use this when you need more control over button content
 */
@Composable
fun CoreButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(ComponentDimens.ButtonHeightMedium),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(
            horizontal = ComponentDimens.ButtonPaddingHorizontal,
            vertical = ComponentDimens.ButtonPaddingVertical
        ),
        content = content
    )
}

/**
 * Tonal Button - Secondary filled button with tonal background
 * Less emphasis than FilledButton
 */
@Composable
fun CoreTonalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.height(ComponentDimens.ButtonHeightMedium),
        enabled = enabled,
        colors = ButtonDefaults.filledTonalButtonColors(),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(
            horizontal = ComponentDimens.ButtonPaddingHorizontal,
            vertical = ComponentDimens.ButtonPaddingVertical
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(ComponentDimens.IconSizeMedium)
            )
            Spacer(modifier = Modifier.width(Spacing.ExtraSmall))
        }
        Text(text = text)
    }
}

/**
 * Outlined Button - Button with border and no background
 * Medium emphasis action
 */
@Composable
fun CoreOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(ComponentDimens.ButtonHeightMedium),
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) {
                MaterialTheme.colorScheme.outline
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            }
        ),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(
            horizontal = ComponentDimens.ButtonPaddingHorizontal,
            vertical = ComponentDimens.ButtonPaddingVertical
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(ComponentDimens.IconSizeMedium)
            )
            Spacer(modifier = Modifier.width(Spacing.ExtraSmall))
        }
        Text(text = text)
    }
}

/**
 * Text Button - Low emphasis button with no background or border
 * Lowest emphasis action
 */
@Composable
fun CoreTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(ComponentDimens.ButtonHeightMedium),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(
            horizontal = ComponentDimens.ButtonPaddingHorizontal,
            vertical = ComponentDimens.ButtonPaddingVertical
        )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(ComponentDimens.IconSizeMedium)
            )
            Spacer(modifier = Modifier.width(Spacing.ExtraSmall))
        }
        Text(text = text)
    }
}

/**
 * Icon Button - Square button containing only an icon
 * Used for toolbar actions and compact spaces
 */
@Composable
fun CoreIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(ComponentDimens.IconSizeMedium)
        )
    }
}

/**
 * Filled Icon Button - Icon button with filled background
 */
@Composable
fun CoreFilledIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(ComponentDimens.IconSizeMedium)
        )
    }
}

/**
 * Tonal Icon Button - Icon button with tonal background
 */
@Composable
fun CoreTonalIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = IconButtonDefaults.filledTonalIconButtonColors()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(ComponentDimens.IconSizeMedium)
        )
    }
}

/**
 * Outlined Icon Button - Icon button with border
 */
@Composable
fun CoreOutlinedIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(ComponentDimens.IconSizeMedium)
        )
    }
}

/**
 * Floating Action Button - Small variant
 * Primary action button that floats above content
 */
@Composable
fun CoreSmallFloatingActionButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SmallFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(ComponentDimens.IconSizeMedium)
        )
    }
}

/**
 * Floating Action Button - Regular variant
 */
@Composable
fun CoreFloatingActionButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(ComponentDimens.IconSizeMedium)
        )
    }
}

/**
 * Extended Floating Action Button - FAB with icon and text
 */
@Composable
fun CoreExtendedFloatingActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = true
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        expanded = expanded,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(ComponentDimens.IconSizeMedium)
            )
        },
        text = { Text(text = text) }
    )
}

/**
 * Segmented Button Group
 * Material Design 3 segmented button for single selection
 *
 * @param options List of option labels
 * @param selectedIndex Currently selected option index
 * @param onOptionSelected Callback when option is selected
 * @param modifier Optional modifier
 */
@Composable
fun <T> CoreSegmentedButtonGroup(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    optionLabel: (T) -> String = { it.toString() }
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = option == selectedOption,
                onClick = { onOptionSelected(option) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                icon = {
                    if (option == selectedOption) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            ) {
                Text(optionLabel(option))
            }
        }
    }
}

/**
 * Loading Button - Primary button with loading state
 * When [isLoading] is true, text is hidden and spinner is shown
 */
@Composable
fun CoreLoadingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(ComponentDimens.ButtonHeightMedium),
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = if (isLoading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = if (isLoading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(
            horizontal = ComponentDimens.ButtonPaddingHorizontal,
            vertical = ComponentDimens.ButtonPaddingVertical
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(ComponentDimens.IconSizeMedium),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(ComponentDimens.IconSizeMedium)
                )
                Spacer(modifier = Modifier.width(Spacing.ExtraSmall))
            }
            Text(text = text)
        }
    }
}