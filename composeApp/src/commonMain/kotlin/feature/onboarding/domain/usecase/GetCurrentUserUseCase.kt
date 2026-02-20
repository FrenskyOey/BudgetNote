package feature.onboarding.domain.usecase

import core.domain.model.Result
import feature.onboarding.domain.model.User
import feature.onboarding.domain.repository.AuthRepository

/**
 * Use case for retrieving the currently authenticated user.
 * 
 * Responsibilities:
 * - Retrieve cached user data from repository
 * - Used for checking login state on app start
 * 
 * @property authRepository Repository for authentication operations
 */
class GetCurrentUserUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Retrieves the currently authenticated user.
     * 
     * @return Result containing User if logged in, null if not logged in, or error
     */
    suspend operator fun invoke(): Result<User?> {
        return authRepository.getCurrentUser()
    }
}
