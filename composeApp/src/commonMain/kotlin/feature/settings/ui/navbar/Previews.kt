package feature.settings.ui.navbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import feature.settings.ui.text.TextScreen

@Preview
@Composable
fun PreviewTextScreen() {
    core.theme.AppTheme {
        NavbarScreen()
    }
}