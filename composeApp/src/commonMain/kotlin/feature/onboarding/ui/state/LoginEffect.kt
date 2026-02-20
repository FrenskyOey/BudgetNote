package feature.onboarding.ui.state

import core.components.SnackbarType

sealed class LoginEffect {
    data class ShowSnackbar(
        val message: String,
        val type: SnackbarType = SnackbarType.ERROR
    ) : LoginEffect()
    
    data class ShowToast(val message: String) : LoginEffect()
}
