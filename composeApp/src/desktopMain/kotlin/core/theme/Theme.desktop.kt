package core.theme

import androidx.compose.runtime.Composable

@Composable
internal actual fun SystemAppearance(isDark: Boolean) {
    // Desktop doesn't need system appearance changes
    // This is typically used for Android status bar colors
}
