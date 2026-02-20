package core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light Mode Colors
val LightPrimary = Color(0xFF138AFC)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFD2E4FF)
val LightOnPrimaryContainer = Color(0xFF001D36)

val LightSecondary = Color(0xFF536E70)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFD7E3F7)
val LightOnSecondaryContainer = Color(0xFF101C2B)

val LightTertiary = Color(0xFF6B5778)
val LightTertiaryContainer = Color(0xFFF3DAFF)

val LightSurface = Color(0xFFF9F9FC)
val LightOnSurface = Color(0xFF1A1C1E)
val LightSurfaceVariant = Color(0xFFE1E2EC)
val LightOutline = Color(0xFF74777F)

val LightError = Color(0xFFBA1A1A)
val LightErrorContainer = Color(0xFFFFDAD6)

// Dark Mode Colors
val DarkPrimary = Color(0xFFD0E4FF)
val DarkOnPrimary = Color(0xFF003258)
val DarkPrimaryContainer = Color(0xFF004977)
val DarkOnPrimaryContainer = Color(0xFFD2E4FF)

val DarkSecondary = Color(0xFFBBC7D8)
val DarkOnSecondary = Color(0xFF253340)
val DarkSecondaryContainer = Color(0xFF3B4958)
val DarkOnSecondaryContainer = Color(0xFFD7E3F7)

val DarkTertiary = Color(0xFFD0BFC0)
val DarkTertiaryContainer = Color(0xFF523F5F)

val DarkSurface = Color(0xFF111318)
val DarkOnSurface = Color(0xFFE2E2E6)
val DarkSurfaceVariant = Color(0xFF44474E)
val DarkOutline = Color(0xFF8E9199)

val DarkError = Color(0xFFFFB4AB)
val DarkErrorContainer = Color(0xFF93000A)

val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    
    tertiary = LightTertiary,
    tertiaryContainer = LightTertiaryContainer,
    
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    outline = LightOutline,
    
    error = LightError,
    errorContainer = LightErrorContainer,
    
    background = LightSurface,
    onBackground = LightOnSurface
)

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    
    tertiary = DarkTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    outline = DarkOutline,
    
    error = DarkError,
    errorContainer = DarkErrorContainer,
    
    background = DarkSurface,
    onBackground = DarkOnSurface
)

// Material 3 Color scheme extension functions
@Composable fun getPrimaryColor() = MaterialTheme.colorScheme.primary
@Composable fun getOnPrimaryColor() = MaterialTheme.colorScheme.onPrimary
@Composable fun getPrimaryContainerColor() = MaterialTheme.colorScheme.primaryContainer
@Composable fun getOnPrimaryContainerColor() = MaterialTheme.colorScheme.onPrimaryContainer

@Composable fun getSecondaryColor() = MaterialTheme.colorScheme.secondary
@Composable fun getOnSecondaryColor() = MaterialTheme.colorScheme.onSecondary
@Composable fun getSecondaryContainerColor() = MaterialTheme.colorScheme.secondaryContainer
@Composable fun getOnSecondaryContainerColor() = MaterialTheme.colorScheme.onSecondaryContainer

@Composable fun getTertiaryColor() = MaterialTheme.colorScheme.tertiary
@Composable fun getTertiaryContainerColor() = MaterialTheme.colorScheme.tertiaryContainer

@Composable fun getBackgroundColor() = MaterialTheme.colorScheme.background
@Composable fun getOnBackgroundColor() = MaterialTheme.colorScheme.onBackground

@Composable fun getSurfaceColor() = MaterialTheme.colorScheme.surface
@Composable fun getOnSurfaceColor() = MaterialTheme.colorScheme.onSurface
@Composable fun getSurfaceVariantColor() = MaterialTheme.colorScheme.surfaceVariant
@Composable fun getOutlineColor() = MaterialTheme.colorScheme.outline

@Composable fun getErrorColor() = MaterialTheme.colorScheme.error
@Composable fun getOnErrorColor() = MaterialTheme.colorScheme.onError
@Composable fun getErrorContainerColor() = MaterialTheme.colorScheme.errorContainer
@Composable fun getOnSurfaceVariantColor() = MaterialTheme.colorScheme.onSurfaceVariant
