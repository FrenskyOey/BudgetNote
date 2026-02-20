package feature.settings.ui.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.components.CoreBackStackAppBar
import core.theme.Dimens
import core.theme.Spacing
import core.theme.getOnBackgroundColor
import core.theme.getOnPrimaryColor
import core.theme.getPrimaryColor
import core.theme.getSurfaceColor
import core.theme.getTextHeadlineSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CoreBackStackAppBar(
                title = "Text Schema",
                onBackPress = onBackClick
            )
        },
        containerColor = getSurfaceColor()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = Dimens.M)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            SectionHeader("Display")
            TypographyItem("DISPLAY LARGE", MaterialTheme.typography.displayLarge)
            TypographyItem("DISPLAY MEDIUM", MaterialTheme.typography.displayMedium)
            TypographyItem("DISPLAY SMALL", MaterialTheme.typography.displaySmall)
            
            SectionHeader("Headline")
            TypographyItem("HEADLINE LARGE", MaterialTheme.typography.headlineLarge)
            TypographyItem("HEADLINE MEDIUM", MaterialTheme.typography.headlineMedium)
            TypographyItem("HEADLINE SMALL", MaterialTheme.typography.headlineSmall)

            SectionHeader("Title")
            TypographyItem("TITLE LARGE", MaterialTheme.typography.titleLarge)
            TypographyItem("TITLE MEDIUM", MaterialTheme.typography.titleMedium)
            TypographyItem("TITLE SMALL", MaterialTheme.typography.titleSmall)

            SectionHeader("Body")
            TypographyItem("BODY LARGE", MaterialTheme.typography.bodyLarge)
            TypographyItem("BODY MEDIUM", MaterialTheme.typography.bodyMedium)
            TypographyItem("BODY SMALL", MaterialTheme.typography.bodySmall)
            
            SectionHeader("Label")
            TypographyItem("LABEL LARGE", MaterialTheme.typography.labelLarge)
            TypographyItem("LABEL MEDIUM", MaterialTheme.typography.labelMedium)
            TypographyItem("LABEL SMALL", MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = getOnBackgroundColor(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    )
}

@Composable
private fun TypographyItem(
    name: String,
    style: TextStyle
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(getOnPrimaryColor())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall.copy(
                color = getPrimaryColor(), // Using primary color for the label as requested (Blue in valid material themes often)
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "The quick fox",
            style = style,
            overflow = TextOverflow.Ellipsis,
            color = getOnBackgroundColor(),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        val weightName = when (style.fontWeight) {
            FontWeight.Normal -> "Regular"
            FontWeight.Medium -> "Medium"
            FontWeight.SemiBold -> "SemiBold"
            FontWeight.Bold -> "Bold"
            else -> "Regular"
        }
        
        val size = style.fontSize.value.let { if (it % 1f == 0f) it.toInt() else it }
        val lineHeight = style.lineHeight.value.let { if (it % 1f == 0f) it.toInt() else it }
        val spacing = style.letterSpacing.value.let { if (it % 1f == 0f) it.toInt() else it }

        Text(
            text = "Work Sans $weightName • $size/$lineHeight • $spacing",
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray,
            )
        )
    }
}