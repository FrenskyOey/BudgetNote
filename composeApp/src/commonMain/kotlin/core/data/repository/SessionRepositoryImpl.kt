package core.data.repository

import com.russhwolf.settings.Settings
import core.domain.repository.SessionRepository
import core.domain.repository.SessionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionRepositoryImpl(
    private val settings: Settings
) : SessionRepository {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Invalid)
    override val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private var lastInvalidationTime = 0L
    private val throttleIntervalMs = 1000L

    init {
        // Check for existing token on initialization
        val token = settings.getStringOrNull("user_token")
        if (!token.isNullOrBlank()) {
            _sessionState.value = SessionState.Valid
        }
    }

    override suspend fun startSession() {
        _sessionState.value = SessionState.Valid
    }

    override suspend fun invalidateSession() {
        val currentTime = core.util.getCurrentTimeMillis()
        if (currentTime - lastInvalidationTime >= throttleIntervalMs) {
            lastInvalidationTime = currentTime
            _sessionState.value = SessionState.Invalid
        }
    }
}
