package feature.onboarding.ui.state

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data class PasswordFocusChanged(val isFocused: Boolean) : LoginEvent()
    object TogglePasswordVisibility : LoginEvent()
    object LoginClicked : LoginEvent()
    object ForgotPasswordClicked : LoginEvent()
    object SignUpClicked : LoginEvent()
    data class GoogleSignIn(val idToken: String, val accessToken: String) : LoginEvent()
}
