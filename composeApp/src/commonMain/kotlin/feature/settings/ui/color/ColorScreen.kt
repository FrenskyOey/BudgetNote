package feature.settings.ui.color

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.theme.DarkColorScheme
import core.theme.LightColorScheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import core.theme.Spacing
import core.components.CoreBackStackAppBar
import core.theme.Dimens
import core.theme.getTextHeadlineSmall
import core.theme.getTextTitleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    val currentScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme

    // Use a local MaterialTheme to apply the selected scheme to this screen's content effectively
    // although we are manually displaying colors, setting the theme ensures backgrounds/text match the mode 
    // if we were using default components. 
    // However, the requirement is to SHOW the colors. 
    // We will stick to the app's scaffold but render the content based on 'currentScheme'.
    
    // To make sure the background of the screen matches the selected mode (White vs Dark Grey/Black),
    // we can wrap the content in a Surface or just use the Scaffold container color if we could.
    // Since Scaffold is already drawn, we might want to just set the background of the content.
    
    val backgroundColor = currentScheme.background
    val onBackgroundColor = currentScheme.onBackground

    Scaffold(
        modifier = modifier,
        topBar = {
            CoreBackStackAppBar(
                title = "Color Schemaa",
                onBackPress = onBackClick
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = Dimens.M)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            // Toggle
            ThemeToggle(
                isDark = isDarkTheme, 
                onToggle = { isDarkTheme = it },
                activeColor = currentScheme.primary,
                inactiveColor = currentScheme.surfaceVariant,
                onActiveColor = currentScheme.onPrimary,
                onInactiveColor = currentScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            ColorGroupTitle("Primary", onBackgroundColor)
            ColorGrid(
                items = listOf(
                    ColorItemData("primary", currentScheme.primary, currentScheme.onPrimary),
                    ColorItemData("on-primary", currentScheme.onPrimary, unspecToWhiteOrBlack(currentScheme.onPrimary)),
                    ColorItemData("primary-container", currentScheme.primaryContainer, currentScheme.onPrimaryContainer),
                    ColorItemData("on-primary-container", currentScheme.onPrimaryContainer, currentScheme.primaryContainer) // Text color usually contrast
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Secondary
            ColorGroupTitle("Secondary", onBackgroundColor)
            ColorGrid(
                items = listOf(
                    ColorItemData("secondary", currentScheme.secondary, currentScheme.onSecondary),
                    ColorItemData("on-secondary", currentScheme.onSecondary, unspecToWhiteOrBlack(currentScheme.onSecondary)),
                    ColorItemData("secondary-container", currentScheme.secondaryContainer, currentScheme.onSecondaryContainer),
                    ColorItemData("on-secondary-container", currentScheme.onSecondaryContainer, currentScheme.secondaryContainer)
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Tertiary
            ColorGroupTitle("Tertiary", onBackgroundColor)
             Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ColorCard(
                    data = ColorItemData("tertiary", currentScheme.tertiary, currentScheme.onTertiary),
                    modifier = Modifier.weight(1f)
                )
                ColorCard(
                    data = ColorItemData("tertiary-container", currentScheme.tertiaryContainer, currentScheme.onTertiaryContainer),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Surface
            ColorGroupTitle("Surface", onBackgroundColor)
            ColorGrid(
                items = listOf(
                    ColorItemData("surface", currentScheme.surface, currentScheme.onSurface),
                    ColorItemData("outline", currentScheme.outline, currentScheme.surface), // Outline text color? 
                    // Actually looking at designs: 
                    // Light: Outline box is Grey, text is White? No text is blackish? 
                    // Let's deduce text color. Usually on-color. 
                    // For now, I will guess best contrast.
                    ColorItemData("surface-variant", currentScheme.surfaceVariant, currentScheme.onSurfaceVariant),
                    ColorItemData("on-surface", currentScheme.onSurface, currentScheme.surface)
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Error
            ColorGroupTitle("Error", onBackgroundColor)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ColorCard(
                    data = ColorItemData("error", currentScheme.error, currentScheme.onError),
                    modifier = Modifier.weight(1f)
                )
                ColorCard(
                    data = ColorItemData("error-container", currentScheme.errorContainer, currentScheme.onErrorContainer),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ThemeToggle(
    isDark: Boolean,
    onToggle: (Boolean) -> Unit,
    activeColor: Color,
    inactiveColor: Color,
    onActiveColor: Color,
    onInactiveColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(inactiveColor.copy(alpha = 0.3f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        ToggleButton(
            text = "Light Mode",
            isSelected = !isDark,
            onClick = { onToggle(false) },
            modifier = Modifier.weight(1f),
            activeColor = activeColor, // Or separate standard color? Design shows white bg for Light Mode active
            textColor = if(!isDark) Color.Black else onInactiveColor // specific logic
        )
        ToggleButton(
            text = "Dark Mode",
            isSelected = isDark,
            onClick = { onToggle(true) },
            modifier = Modifier.weight(1f),
            activeColor = activeColor, // Design shows dark grey bg for Dark Mode active
            textColor = if(isDark) Color.White else onInactiveColor
        )
    }
}

@Composable
fun ToggleButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color,
    textColor: Color
) {
    val backgroundColor = if (isSelected) Color.White.takeIf { text == "Light Mode" } ?: Color(0xFF333333) else Color.Transparent 
    // The design is a bit specific:
    // Light Mode Active: White bg, Blue text? No, looks like standard text.
    // Let's simplify:
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) (if(text == "Light Mode") Color.White else Color(0xFF3B4958)) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = getTextTitleMedium().copy(
                fontSize = 12.sp, 
                fontWeight = FontWeight.Bold,
                color = if (isSelected) (if(text == "Light Mode") Color(0xFF138AFC) else Color(0xFFD0E4FF)) else Color.Gray// Heuristic based on design
            )
        )
    }
}

@Composable
fun ColorGroupTitle(text: String, color: Color) {
    Text(
        text = text,
        style = getTextTitleMedium().copy(fontWeight = FontWeight.Bold, color = color),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun ColorGrid(items: List<ColorItemData>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColorCard(items[0], Modifier.weight(1f))
            ColorCard(items[1], Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColorCard(items[2], Modifier.weight(1f))
            ColorCard(items[3], Modifier.weight(1f))
        }
    }
}

@Composable
fun ColorCard(data: ColorItemData, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f) // Square
            .clip(RoundedCornerShape(8.dp))
            .background(data.color)
            .padding(12.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Column {
            Text(
                text = data.name,
                style = getTextTitleMedium().copy(
                    fontSize = 12.sp, 
                    fontWeight = FontWeight.Bold,
                    color = data.onColor
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = data.color.toHex(),
                style = getTextTitleMedium().copy(
                    fontSize = 10.sp,
                    color = data.onColor.copy(alpha = 0.8f)
                )
            )
        }
    }
}

data class ColorItemData(val name: String, val color: Color, val onColor: Color)

fun Color.toHex(): String {
    val alpha = this.alpha
    val red = this.red
    val green = this.green
    val blue = this.blue
    
    // Convert float 0-1 to int 0-255
    val r = (red * 255).toInt()
    val g = (green * 255).toInt()
    val b = (blue * 255).toInt()
    
    // We can use a simple manual mapping since String.format is missing in commonMain
    fun Int.toHexString(): String {
        val chars = "0123456789ABCDEF"
        return "${chars[this shr 4]}${chars[this and 0x0F]}"
    }

    return "#${r.toHexString()}${g.toHexString()}${b.toHexString()}"
}

// Helper to deduce onColor if not strictly provided (just a fallback, though we passed explicit ones)
fun unspecToWhiteOrBlack(color: Color): Color {
    // Determine luminance
    val luminance = (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue)
    return if (luminance > 0.5) Color.Black else Color.White
}