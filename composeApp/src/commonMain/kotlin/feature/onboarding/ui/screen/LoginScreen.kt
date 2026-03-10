package feature.onboarding.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import core.components.CoreBasicAppBar
import core.components.CoreSnackbarHost
import core.components.SnackbarType
import core.components.showSnackbar
import core.theme.Spacing
import feature.onboarding.ui.components.GoogleSignInForm
import feature.onboarding.ui.components.LoginForm
import feature.onboarding.ui.components.LoginFormState
import feature.onboarding.ui.components.LoginHeader
import feature.onboarding.ui.state.LoginEffect
import feature.onboarding.ui.state.LoginEvent
import feature.onboarding.ui.state.LoginState
import feature.onboarding.ui.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        type = effect.type
                    )
                }
                is LoginEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        type = SnackbarType.NORMAL,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    LoginPage(
        state = state,
        snackbarHostState = snackbarHostState,
        onEvent = {event -> viewModel.onEvent(event)},
    )
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CoreBasicAppBar(title = "Log In")
        },
        snackbarHost = {
            CoreSnackbarHost(
                hostState = snackbarHostState,
                modifier = modifier
                    .imePadding() // Move up with keyboard
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = Spacing.Large)
                .imePadding(), // Handle keyboard overlap
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Spacing.ExtraLarge))

            LoginHeader()

            Spacer(modifier = Modifier.height(Spacing.ExtraLarge))

            // Optimized: Use stable state object to reduce recomposition
            val formState = remember(state) {
                LoginFormState(
                    email = state.email,
                    password = state.password,
                    emailError = state.emailError,
                    passwordError = state.passwordError,
                    isPasswordVisible = state.isPasswordVisible,
                    isPasswordFocused = state.isPasswordFocused,
                    isLoading = state.isLoading
                )
            }

            LoginForm(
                state = formState,
                onEmailChange = { onEvent(LoginEvent.EmailChanged(it)) },
                onPasswordChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                onPasswordFocusChange = { onEvent(LoginEvent.PasswordFocusChanged(it)) },
                onTogglePasswordVisibility = { onEvent(LoginEvent.TogglePasswordVisibility) },
                onLoginClick = { onEvent(LoginEvent.LoginClicked) },
                onForgotPasswordClick = { onEvent(LoginEvent.ForgotPasswordClicked) },
            )

            Spacer(modifier = Modifier.height(Spacing.Large))

            GoogleSignInForm(
                isLoading = state.isLoading,
                onGoogleSignInClick = { onEvent(LoginEvent.GoogleSignIn(idToken = "", accessToken = "")) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Spacing.Large))
        }
    }
}

// ─── Previews ────────────────────────────────────────────────────────────────

@androidx.compose.ui.tooling.preview.Preview(name = "Login – Default (empty form)")
@Composable
private fun PreviewLoginPageDefault() {
    LoginPage(
        state = LoginState(),
        onEvent = {},
        snackbarHostState = SnackbarHostState()
    )
}

@androidx.compose.ui.tooling.preview.Preview(name = "Login – Filled form")
@Composable
private fun PreviewLoginPageFilled() {
    LoginPage(
        state = LoginState(
            email = "user@email.com",
            password = "Password123"
        ),
        onEvent = {},
        snackbarHostState = SnackbarHostState()
    )
}

@androidx.compose.ui.tooling.preview.Preview(name = "Login – Validation errors")
@Composable
private fun PreviewLoginPageErrors() {
    LoginPage(
        state = LoginState(
            email = "invalid-email",
            emailError = "Invalid email format",
            password = "123",
            passwordError = "Password must be at least 8 characters"
        ),
        onEvent = {},
        snackbarHostState = SnackbarHostState()
    )
}

@androidx.compose.ui.tooling.preview.Preview(name = "Login – Loading")
@Composable
private fun PreviewLoginPageLoading() {
    LoginPage(
        state = LoginState(
            email = "user@email.com",
            password = "Password123",
            isLoading = true
        ),
        onEvent = {},
        snackbarHostState = SnackbarHostState()
    )
}
