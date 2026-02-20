package feature.onboarding.domain.usecase

import feature.onboarding.domain.repository.AuthRepository

/**
 * Use case for checking if a user is currently logged in.
 * 
 * Responsibilities:
 * - Quick check if user is logged in (valid token exists)
 * - Used for navigation routing on app start
 * 
 * @property authRepository Repository for authentication operations
 */
class CheckLoginStatusUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Checks if a user is currently logged in.
     * 
     * @return true if user is logged in, false otherwise
     */
    suspend operator fun invoke(): Boolean {
        return authRepository.isLoggedIn()
    }
}
