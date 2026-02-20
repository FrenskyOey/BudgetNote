package core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import budgetnote.composeapp.generated.resources.Res
import budgetnote.composeapp.generated.resources.work_sans_bold
import budgetnote.composeapp.generated.resources.work_sans_medium
import budgetnote.composeapp.generated.resources.work_sans_regular
import budgetnote.composeapp.generated.resources.work_sans_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun AppTypography(): Typography {
    val WorkSansFontFamily = FontFamily(
        Font(Res.font.work_sans_regular, FontWeight.Normal),
        Font(Res.font.work_sans_medium, FontWeight.Medium),
        Font(Res.font.work_sans_semibold, FontWeight.SemiBold),
        Font(Res.font.work_sans_bold, FontWeight.Bold)
    )

    return Typography(
        // Display Styles
        displayLarge = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp
        ),
        displayMedium = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp
        ),

        // Headline Styles
        headlineLarge = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),

        // Title Styles
        titleLarge = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        titleSmall = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),

        // Body Styles
        bodyLarge = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),

        // Label Styles
        labelLarge = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = WorkSansFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}

// Material 3 Typography helpers with extension functions
@Composable fun getTextDisplayLarge() = MaterialTheme.typography.displayLarge
@Composable fun getTextDisplayMedium() = MaterialTheme.typography.displayMedium
@Composable fun getTextDisplaySmall() = MaterialTheme.typography.displaySmall
@Composable fun getTextHeadlineLarge() = MaterialTheme.typography.headlineLarge
@Composable fun getTextHeadlineMedium() = MaterialTheme.typography.headlineMedium
@Composable fun getTextHeadlineSmall() = MaterialTheme.typography.headlineSmall
@Composable fun getTextTitleLarge() = MaterialTheme.typography.titleLarge
@Composable fun getTextTitleMedium() = MaterialTheme.typography.titleMedium
@Composable fun getTextTitleSmall() = MaterialTheme.typography.titleSmall
@Composable fun getTextBodyLarge() = MaterialTheme.typography.bodyLarge
@Composable fun getTextBodyMedium() = MaterialTheme.typography.bodyMedium
@Composable fun getTextBodySmall() = MaterialTheme.typography.bodySmall
@Composable fun getTextLabelLarge() = MaterialTheme.typography.labelLarge
@Composable fun getTextLabelMedium() = MaterialTheme.typography.labelMedium
@Composable fun getTextLabelSmall() = MaterialTheme.typography.labelSmall