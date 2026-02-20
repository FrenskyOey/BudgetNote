package feature.settings.ui.color

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

// Preview for SettingScreen with sample data
@Preview
@Composable
fun PreviewColorScreen() {
    core.theme.AppTheme {
        feature.settings.ui.color.ColorScreen()
    }
}