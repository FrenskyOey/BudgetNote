package core.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import core.theme.Spacing

/**
 * Snackbar Type - Defines the visual style of the snackbar
 */
enum class SnackbarType {
    NORMAL,
    ERROR,
    WARNING
}

/**
 * Core Snackbar Host - Custom snackbar host that supports different variants
 */
@Composable
fun CoreSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier
    ) { data ->
        val type = when (data.visuals) {
            is CustomSnackbarVisuals -> (data.visuals as CustomSnackbarVisuals).type
            else -> SnackbarType.NORMAL
        }

        val containerColor = when (type) {
            SnackbarType.NORMAL -> MaterialTheme.colorScheme.inverseSurface
            SnackbarType.ERROR -> MaterialTheme.colorScheme.error
            SnackbarType.WARNING -> MaterialTheme.colorScheme.tertiary
        }

        val contentColor = when (type) {
            SnackbarType.NORMAL -> MaterialTheme.colorScheme.inverseOnSurface
            SnackbarType.ERROR -> MaterialTheme.colorScheme.onError
            SnackbarType.WARNING -> MaterialTheme.colorScheme.onTertiary
        }

        Snackbar(
            snackbarData = data,
            containerColor = containerColor,
            contentColor = contentColor,
            actionColor = contentColor,
            shape = MaterialTheme.shapes.small
        )
    }
}

/**
 * Custom Visuals implementation to pass type through SnackbarHostState
 */
private class CustomSnackbarVisuals(
    override val message: String,
    override val actionLabel: String?,
    override val duration: SnackbarDuration,
    override val withDismissAction: Boolean,
    val type: SnackbarType
) : SnackbarVisuals

/**
 * Extension function to show snackbar with type
 */
suspend fun SnackbarHostState.showSnackbar(
    message: String,
    type: SnackbarType = SnackbarType.NORMAL,
    duration: SnackbarDuration = SnackbarDuration.Short,
    actionLabel: String? = null,
    withDismissAction: Boolean = false
): SnackbarResult {
    return showSnackbar(
        CustomSnackbarVisuals(
            message = message,
            actionLabel = actionLabel,
            duration = duration,
            withDismissAction = withDismissAction,
            type = type
        )
    )
}
