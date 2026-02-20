package feature.onboarding.domain.repository

import core.domain.model.Result
import feature.onboarding.domain.model.LoginCredentials
import feature.onboarding.domain.model.User

/**
 * Repository interface for authentication operations.
 * 
 * This interface abstracts authentication operations from the domain layer,
 * allowing the data layer to handle implementation details.
 */
interface AuthRepository {
    /**
     * Authenticates user with the provided credentials.
     * 
     * @param credentials Login credentials containing email and hashed password
     * @return Result containing User data on success, or error on failure
     */
    suspend fun login(credentials: LoginCredentials): Result<User>
    
    /**
     * Logs out the current user by clearing stored token and user data.
     * 
     * @return Result indicating success or failure of logout operation
     */
    suspend fun logout(): Result<Unit>
    
    /**
     * Retrieves the currently authenticated user from cache.
     * 
     * @return Result containing User if logged in, null if not logged in, or error
     */
    suspend fun getCurrentUser(): Result<User?>
    
    /**
     * Checks if a user is currently logged in (valid token exists).
     * 
     * @return true if user is logged in, false otherwise
     */
    suspend fun isLoggedIn(): Boolean
    
}
