package core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import core.theme.*

@Composable
fun CoreAvatar(
    imageUrl: String?,
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = ComponentDimens.AvatarSizeMedium, // 40.dp
    backgroundColor: Color = getPrimaryContainerColor(),
    contentColor: Color = getOnPrimaryContainerColor()
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Avatar for $name",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
                // Optional: Add placeholder/error handling if needed, 
                // but for avatar usually fallback to initials on error is tricky without state,
                // so we rely on basic AsyncImage for now. 
                // If load fails, it might be empty. 
                // Ideally we'd listen to state, but for simplicity:
            )
        } else {
            val initials = getInitials(name)
            Text(
                text = initials,
                style = getTextTitleMedium(),
                color = contentColor
            )
        }
    }
}

private fun getInitials(name: String): String {
    if (name.isBlank()) return ""
    val parts = name.trim().split("\\s+".toRegex()).take(2)
    return parts.joinToString("") { it.first().uppercase() }
}
