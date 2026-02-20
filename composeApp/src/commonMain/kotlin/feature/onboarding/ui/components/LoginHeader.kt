package feature.onboarding.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import core.theme.*

/**
 * Login Header - App icon and welcome text
 */
@Composable
fun LoginHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Icon with blue rounded background
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(getPrimaryColor()), // Blue background
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Receipt,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(Spacing.Large))
        
        // App Title
        Text(
            text = "News Feedz",
            style = getTextHeadlineLarge().copy(
                fontWeight = FontWeight.Bold,
                fontSize = getTextHeadlineLarge().fontSize
            ),
            color = getOnBackgroundColor(),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Spacing.Small))
        
        // Tagline
        Text(
            text = "Catch up with the world",
            style = getTextBodyLarge(),
            color = getOnSurfaceVariantColor(),
            textAlign = TextAlign.Center
        )
    }
}
