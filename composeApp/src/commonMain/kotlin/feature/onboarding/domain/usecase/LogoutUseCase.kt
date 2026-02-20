package feature.onboarding.domain.usecase

import core.domain.model.Result
import feature.onboarding.domain.repository.AuthRepository

/**
 * Use case for logging out the current user.
 * 
 * Responsibilities:
 * - Call repository to clear token and user data
 * 
 * @property authRepository Repository for authentication operations
 */
class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Logs out the current user.
     * 
     * @return Result indicating success or failure of logout operation
     */
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logout()
    }
}
