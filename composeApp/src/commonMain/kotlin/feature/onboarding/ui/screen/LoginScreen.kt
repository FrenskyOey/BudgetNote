package feature.onboarding.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import feature.onboarding.ui.components.LoginFooter
import feature.onboarding.ui.components.LoginForm
import feature.onboarding.ui.components.LoginFormState
import feature.onboarding.ui.components.LoginHeader
import feature.onboarding.ui.components.PasswordRequirements
import feature.onboarding.ui.state.LoginEffect
import feature.onboarding.ui.state.LoginEvent
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

    androidx.compose.material3.Scaffold(
        topBar = {
            CoreBasicAppBar(title = "Log In")
        },
        snackbarHost = {
            CoreSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
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
                onEmailChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
                onPasswordChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                onPasswordFocusChange = { viewModel.onEvent(LoginEvent.PasswordFocusChanged(it)) },
                onTogglePasswordVisibility = { viewModel.onEvent(LoginEvent.TogglePasswordVisibility) },
                onLoginClick = { viewModel.onEvent(LoginEvent.LoginClicked) },
                onForgotPasswordClick = { viewModel.onEvent(LoginEvent.ForgotPasswordClicked) }
            )

            Spacer(modifier = Modifier.height(Spacing.Large))
            
            LoginFooter(
                onSignUpClick = { viewModel.onEvent(LoginEvent.SignUpClicked) }
            )
            
            Spacer(modifier = Modifier.height(Spacing.Large))
        }
    }
}
