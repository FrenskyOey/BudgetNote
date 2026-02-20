package feature.settings.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import feature.settings.ui.text.TextScreen

// Preview for SettingScreen with sample data
@Preview
@Composable
fun PreviewSettingScreen() {
    core.theme.AppTheme {
        SettingScreen(){

        }
    }
}


