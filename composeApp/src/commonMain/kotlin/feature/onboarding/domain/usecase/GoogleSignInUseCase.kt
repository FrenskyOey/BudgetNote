package feature.onboarding.domain.usecase

import core.domain.model.Result
import feature.onboarding.domain.model.User
import feature.onboarding.domain.repository.AuthRepository

/**
 * Use case for handling Google SSO authentication.
 *
 * Responsibilities:
 * 1. Receive the Google ID token and access token from the UI layer
 * 2. Delegate to the repository to perform Supabase Google sign-in
 * 3. Return the authenticated User or propagate the error
 *
 * Note: Token validation is handled by Supabase on the server side,
 * so no client-side validation is performed here.
 *
 * @property authRepository Repository for authentication operations
 */
class GoogleSignInUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Authenticates a user via Google SSO.
     *
     * @param idToken The Google ID token obtained from the Google Sign-In SDK
     * @param accessToken The Google access token obtained from the Google Sign-In SDK
     * @return Result containing User on success, or error on failure
     */
    suspend operator fun invoke(idToken: String, accessToken: String): Result<User> {
        return authRepository.loginWithGoogle(idToken, accessToken)
    }
}
