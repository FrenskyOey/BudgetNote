package core.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import core.theme.*
import kotlin.math.roundToInt

/**
 * Slider with Label and Value Display
 * Material Design 3 slider with customizable value display
 *
 * @param value Current slider value
 * @param onValueChange Callback when value changes
 * @param label Label text shown above slider
 * @param modifier Optional modifier
 * @param valueRange Range of valid values (default: 0f..100f)
 * @param steps Number of discrete steps (0 for continuous)
 * @param enabled Whether slider is enabled
 * @param showValue Whether to show current value
 * @param valueFormatter Function to format value for display
 * @param helperText Optional helper text shown below slider
 */

 /**
 * Simple Slider (minimal version)
 * Basic slider without label or value display
 */
@Composable
fun CoreSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = null,
        interactionSource = interactionSource,
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.primary,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

@Composable
fun CoreLabelSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    steps: Int = 0,
    enabled: Boolean = true,
    showValue: Boolean = true,
    valueFormatter: (Float) -> String = { "${it.roundToInt()}%" },
    helperText: String? = null
) {
    Column(modifier = modifier) {
        // Label and Value Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                }
            )

            if (showValue) {
                Text(
                    text = valueFormatter(value),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.Small))

        // Slider
        CoreSlider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            valueRange = valueRange,
            steps = steps,
        )

        // Helper Text
        if (helperText != null) {
            Text(
                text = helperText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Spacing.Tiny)
            )
        }
    }
}

/**
 * Volume/Intensity Slider
 * Pre-configured slider for volume or intensity controls (0-100%)
 */
@Composable
fun CorePercentSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String = "Volume Intensity",
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    CoreLabelSlider(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        valueRange = 0f..100f,
        steps = 0,
        enabled = enabled,
        showValue = true,
        valueFormatter = { "${it.roundToInt()}%" }
    )
}

/**
 * Discrete Slider
 * Slider with specific step intervals
 */
@Composable
fun CoreRangeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..10f,
    steps: Int = 9, // For 0-10 range with 10 values, use 9 steps
    enabled: Boolean = true,
    valueFormatter: (Float) -> String = { it.roundToInt().toString() }
) {
    CoreLabelSlider(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        valueRange = valueRange,
        steps = steps,
        enabled = enabled,
        showValue = true,
        valueFormatter = valueFormatter
    )
}




/**
 * Custom Slider with Min/Max Labels
 * Slider with labels showing minimum and maximum values
 */
@Composable
fun CoreSliderMinMax(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    steps: Int = 0,
    enabled: Boolean = true,
    minLabel: String = valueRange.start.roundToInt().toString(),
    maxLabel: String = valueRange.endInclusive.roundToInt().toString(),
    valueFormatter: (Float) -> String = { "${it.roundToInt()}" }
) {
    Column(modifier = modifier) {
        // Label and Current Value
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                }
            )

            Text(
                text = valueFormatter(value),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(Spacing.Small))

        // Slider
        CoreSlider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            valueRange = valueRange,
            steps = steps
        )

        // Min and Max Labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = minLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start
            )

            Text(
                text = maxLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End
            )
        }
    }
}