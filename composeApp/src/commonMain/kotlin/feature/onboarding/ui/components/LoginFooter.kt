package feature.onboarding.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import core.theme.*
import core.theme.getTextBodyMedium

/**
 * Login Footer - Sign up link
 */
@Composable
fun LoginFooter(
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Don't have an account?",
            style = getTextBodyMedium(),
            color = getOnSurfaceVariantColor()
        )
        
        Spacer(modifier = Modifier.width(Spacing.Tiny))
        
        Text(
            text = "Sign Up",
            style = getTextLabelLarge().copy(fontWeight = FontWeight.Bold),
            color = getPrimaryColor(),
            modifier = Modifier.clickable(onClick = onSignUpClick)
        )
    }
}
