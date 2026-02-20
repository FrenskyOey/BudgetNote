package core.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Dimens {
    // Extra Small Sizes
    val XXS: Dp = 2.dp
    val XS: Dp = 4.dp

    // Small Sizes
    val S: Dp = 8.dp
    val SM: Dp = 12.dp  // Small-Medium

    // Medium Size (Base reference)
    val M: Dp = 16.dp

    // Large Sizes
    val ML: Dp = 20.dp  // Medium-Large
    val L: Dp = 24.dp

    // Extra Large Sizes
    val XL: Dp = 32.dp
    val XXL: Dp = 40.dp
    val XXXL: Dp = 48.dp

    // Huge Sizes
    val H: Dp = 56.dp
    val XH: Dp = 64.dp
    val XXH: Dp = 72.dp
    val XXXH: Dp = 80.dp
}

/**
 * Semantic spacing names for common use cases
 * These map to the t-shirt sizes above but provide context
 */
object Spacing {
    // Minimal spacing
    val Micro: Dp = Dimens.XXS      // 2dp - for subtle separations
    val Tiny: Dp = Dimens.XS        // 4dp - for icon spacing or small text blocks

    // Small spacing
    val ExtraSmall: Dp = Dimens.S   // 8dp - for compact layouts
    val Small: Dp = Dimens.SM       // 12dp - for list item padding

    // Medium spacing (most common)
    val Medium: Dp = Dimens.M       // 16dp - standard padding
    val MediumPlus: Dp = Dimens.ML  // 20dp - slightly more breathing room

    // Large spacing
    val Large: Dp = Dimens.L        // 24dp - section spacing
    val ExtraLarge: Dp = Dimens.XL  // 32dp - major sections

    // Extra large spacing
    val Huge: Dp = Dimens.XXL       // 40dp - for hero sections
    val Massive: Dp = Dimens.XXXL   // 48dp - for major visual breaks
    val Giant: Dp = Dimens.H        // 56dp - for large components
    val Enormous: Dp = Dimens.XH    // 64dp - for extra large components
    val Colossal: Dp = Dimens.XXH   // 72dp - for maximum spacing
    val Maximum: Dp = Dimens.XXXH   // 80dp - for maximum spacing
}

/**
 * Component-specific dimensions
 */
object ComponentDimens {
    // Button dimensions
    val ButtonHeightSmall: Dp = 32.dp
    val ButtonHeightMedium: Dp = 40.dp
    val ButtonHeightLarge: Dp = 48.dp
    val ButtonPaddingHorizontal: Dp = Dimens.L  // 24dp
    val ButtonPaddingVertical: Dp = Dimens.S    // 8dp

    // Icon dimensions
    val IconSizeSmall: Dp = 16.dp
    val IconSizeMedium: Dp = 24.dp
    val IconSizeLarge: Dp = 32.dp
    val IconSizeExtraLarge: Dp = 48.dp

    // Card dimensions
    val CardElevation: Dp = 2.dp
    val CardCornerRadius: Dp = 12.dp
    val CardPadding: Dp = Dimens.M  // 16dp

    // Input field dimensions
    val TextFieldHeight: Dp = 56.dp
    val TextFieldPadding: Dp = Dimens.M  // 16dp
    val TextFieldCornerRadius: Dp = 4.dp

    // Divider
    val DividerThickness: Dp = 1.dp

    // Bottom sheet
    val BottomSheetPeekHeight: Dp = 64.dp
    val BottomSheetCornerRadius: Dp = 16.dp

    // Dialog
    val DialogPadding: Dp = Dimens.L  // 24dp
    val DialogCornerRadius: Dp = 28.dp
    val DialogMinWidth: Dp = 280.dp
    val DialogMaxWidth: Dp = 560.dp

    // App bar
    val TopAppBarHeight: Dp = 64.dp
    val BottomAppBarHeight: Dp = 80.dp

    // List items
    val ListItemHeight: Dp = 56.dp
    val ListItemHeightTwoLine: Dp = 72.dp
    val ListItemHeightThreeLine: Dp = 88.dp

    // FAB (Floating Action Button)
    val FabSizeSmall: Dp = 40.dp
    val FabSizeMedium: Dp = 56.dp
    val FabSizeLarge: Dp = 96.dp

    // Chip
    val ChipHeight: Dp = 32.dp
    val ChipPaddingHorizontal: Dp = Dimens.SM  // 12dp

    // Avatar
    val AvatarSizeSmall: Dp = 24.dp
    val AvatarSizeMedium: Dp = 40.dp
    val AvatarSizeLarge: Dp = 56.dp
    val AvatarSizeExtraLarge: Dp = 96.dp
}

/**
 * Border radius values
 */
object BorderRadius {
    val None: Dp = 0.dp
    val Small: Dp = 4.dp
    val Medium: Dp = 8.dp
    val Large: Dp = 12.dp
    val ExtraLarge: Dp = 16.dp
    val XXLarge: Dp = 24.dp
    val Round: Dp = 28.dp
    val Circle: Dp = 50.dp  // Use with .clip(CircleShape) for perfect circles
}

/**
 * Elevation values for Material Design 3
 */
object Elevation {
    val Level0: Dp = 0.dp   // No elevation
    val Level1: Dp = 1.dp   // Subtle elevation
    val Level2: Dp = 3.dp   // Card elevation
    val Level3: Dp = 6.dp   // Raised elements
    val Level4: Dp = 8.dp   // Navigation drawer
    val Level5: Dp = 12.dp  // Modal dialogs
}

// Extension functions for easy access in composables
object DimensExtensions {
    val Int.extraSmall: Dp get() = (this * Dimens.S.value).dp
    val Int.small: Dp get() = (this * Dimens.SM.value).dp
    val Int.medium: Dp get() = (this * Dimens.M.value).dp
    val Int.large: Dp get() = (this * Dimens.L.value).dp
    val Int.extraLarge: Dp get() = (this * Dimens.XL.value).dp
}
