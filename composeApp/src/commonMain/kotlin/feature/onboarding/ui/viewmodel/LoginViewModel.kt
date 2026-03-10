package feature.onboarding.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.model.Result
import core.domain.repository.SessionRepository
import feature.onboarding.domain.usecase.GoogleSignInUseCase
import feature.onboarding.domain.usecase.LoginUseCase
import feature.onboarding.domain.usecase.ValidateEmailUseCase
import feature.onboarding.domain.usecase.ValidatePasswordUseCase
import feature.onboarding.ui.state.LoginEffect
import feature.onboarding.ui.state.LoginEvent
import feature.onboarding.ui.state.LoginState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val sessionRepository: SessionRepository,
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect: Flow<LoginEffect> = _effect.receiveAsFlow()

    init {
        setupValidation()
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email) }
            }
            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password) }
            }
            is LoginEvent.PasswordFocusChanged -> {
                _state.update { it.copy(isPasswordFocused = event.isFocused) }
            }
            is LoginEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            is LoginEvent.LoginClicked -> {
                performLogin()
            }
            is LoginEvent.ForgotPasswordClicked -> {
                sendEffect(LoginEffect.ShowToast("Coming Soon: Forgot Password"))
            }
            is LoginEvent.SignUpClicked -> {
                sendEffect(LoginEffect.ShowToast("Coming Soon: Sign Up"))
            }
            is LoginEvent.GoogleSignIn -> {
                performGoogleSignIn(event.idToken, event.accessToken)
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun setupValidation() {
        // Email validation
        _state.map { it.email }
            .distinctUntilChanged()
            .drop(1) // Skip initial empty state
            .debounce(500)
            .onEach { email ->
                val result = validateEmailUseCase(email)
                if (result is Result.Error) {
                    _state.update { it.copy(emailError = result.exception.message) }
                } else {
                    _state.update { it.copy(emailError = null) }
                }
            }
            .launchIn(viewModelScope)

        // Password validation
        _state.map { it.password }
            .distinctUntilChanged()
            .drop(1) // Skip initial empty state
            .debounce(500)
            .onEach { password ->
                val result = validatePasswordUseCase(password)
                if (result is Result.Error) {
                    _state.update { it.copy(passwordError = result.exception.message) }
                } else {
                    _state.update { it.copy(passwordError = null) }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun performLogin() {
        val currentState = _state.value
        val emailValidation = validateEmailUseCase(currentState.email)
        val passwordValidation = validatePasswordUseCase(currentState.password)

        val emailError = (emailValidation as? Result.Error)?.exception?.message
        val passwordError = (passwordValidation as? Result.Error)?.exception?.message

        if (emailError != null || passwordError != null) {
            _state.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = loginUseCase(currentState.email, currentState.password)) {
                is Result.Success -> {
                    // Start the session strictly. This updates SessionRepository state to Valid.
                    // App.kt observes this state change and automatically navigates to the Dashboard.
                    // We do NOT navigate manually here to avoid race conditions.
                    sessionRepository.startSession()
                    _state.update { it.copy(isLoading = false) }
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(LoginEffect.ShowSnackbar(result.exception.message ?: "Unknown error"))
                }
                else -> {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(LoginEffect.ShowSnackbar("Unexpected login result"))
                }
            }
        }
    }

    private fun performGoogleSignIn(idToken: String, accessToken: String) {
        // Guard against concurrent sign-in attempts
        if (_state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = googleSignInUseCase(idToken, accessToken)) {
                is Result.Success -> {
                    // Same pattern as email/password: start session, let App.kt handle navigation.
                    sessionRepository.startSession()
                    _state.update { it.copy(isLoading = false) }
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(LoginEffect.ShowSnackbar(result.exception.message ?: "Google sign-in failed"))
                }
                else -> {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(LoginEffect.ShowSnackbar("Unexpected result from Google sign-in"))
                }
            }
        }
    }

    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}
