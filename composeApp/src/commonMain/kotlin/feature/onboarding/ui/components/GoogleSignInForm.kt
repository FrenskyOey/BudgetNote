package feature.onboarding.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import budgetnote.composeapp.generated.resources.Res
import budgetnote.composeapp.generated.resources.ic_google
import org.jetbrains.compose.resources.painterResource
import core.theme.*

/**
 * Google Sign-In section — OR divider + "Continue with Google" button.
 * The button uses a centred [Box] for content so the Google G icon and
 * label text are always visually centred regardless of button width.
 *
 * @param isLoading  Disables the button while a sign-in is in progress.
 * @param onGoogleSignInClick  Triggered when the user taps the button.
 */
@Composable
fun GoogleSignInForm(
    isLoading: Boolean = false,
    onGoogleSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ── OR Divider ────────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "  OR  ",
                style = getTextLabelMedium(),
                color = getOnSurfaceVariantColor()
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(Spacing.Large))

        // ── Google Sign-In Button ─────────────────────────────────────────────
        OutlinedButton(
            onClick = onGoogleSignInClick,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(ComponentDimens.ButtonHeightMedium),
            shape = MaterialTheme.shapes.small,
            border = BorderStroke(
                width = ComponentDimens.DividerThickness,
                color = if (isLoading) {
                    getOutlineColor().copy(alpha = 0.38f)
                } else {
                    getOutlineColor()
                }
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = getOnSurfaceColor()
            )
        ) {
            // Centred row: [G icon] [space] [label]
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_google),
                        contentDescription = "Google logo",
                        modifier = Modifier.size(ComponentDimens.IconSizeMedium),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(Spacing.Small))
                    Text(
                        text = "Continue with Google",
                        style = getTextBodyMedium()
                    )
                }
            }
        }
    }
}
