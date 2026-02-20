package core.domain.repository

import kotlinx.coroutines.flow.StateFlow

sealed class SessionState {
    data object Valid : SessionState()
    data object Invalid : SessionState()
}

interface SessionRepository {
    val sessionState: StateFlow<SessionState>
    suspend fun startSession()
    suspend fun invalidateSession()
}
