package feature.onboarding.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import core.components.CoreEmailInput
import core.components.CoreLoadingButton
import core.components.CorePasswordInput
import core.theme.*

/**
 * Login Form State - Stable state object to reduce recomposition
 */
@Stable
data class LoginFormState(
    val email: String,
    val password: String,
    val emailError: String?,
    val passwordError: String?,
    val isPasswordVisible: Boolean,
    val isPasswordFocused: Boolean,
    val isLoading: Boolean
)

/**
 * Login Form - Email and Password fields with Login button
 */
@Composable
fun LoginForm(
    state: LoginFormState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordFocusChange: (Boolean) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        // Email Field
        CoreEmailInput(
            value = state.email,
            onValueChange = onEmailChange,
            errorText = state.emailError,
            enabled = !state.isLoading,
            onClearClick = if (state.email.isNotEmpty()) { { onEmailChange("") } } else null,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(Spacing.Large))
        
        // Password Field
        CorePasswordInput(
            value = state.password,
            onValueChange = onPasswordChange,
            errorText = state.passwordError,
            enabled = !state.isLoading,
            isPasswordVisible = state.isPasswordVisible,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    onPasswordFocusChange(focusState.isFocused)
                }
        )
        
        Spacer(modifier = Modifier.height(Spacing.ExtraSmall))

        PasswordRequirements(
            password = state.password,
            isVisible = state.isPasswordFocused
        )

        Spacer(modifier = Modifier.height(Spacing.Medium))
        // Forgot Password
        Text(
            text = "Forgot?",
            style = getTextBodyMedium().copy(fontWeight = FontWeight.Bold),
            color = getPrimaryColor(),
            modifier = Modifier
                .clickable(
                    enabled = !state.isLoading,
                    onClick = onForgotPasswordClick
                )
                .padding(vertical = Spacing.Tiny)
        )
        
        Spacer(modifier = Modifier.height(Spacing.Large))
        
        // Login Button
        CoreLoadingButton(
            text = "Log In",
            onClick = onLoginClick,
            isLoading = state.isLoading,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
