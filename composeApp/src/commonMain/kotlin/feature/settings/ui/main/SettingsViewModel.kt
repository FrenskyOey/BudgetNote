package feature.settings.ui.main

import androidx.lifecycle.ViewModel
import feature.settings.domain.usecase.GetSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    private val getSettingsUseCase: GetSettingsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow("Loading")
    val uiState: StateFlow<String> = _uiState

    // Placeholder logic
}