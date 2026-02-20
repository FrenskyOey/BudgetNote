package feature.onboarding.domain.usecase

import core.domain.model.AppException
import core.domain.model.Result
import core.util.HashUtil
import feature.onboarding.domain.model.LoginCredentials
import feature.onboarding.domain.model.User
import feature.onboarding.domain.repository.AuthRepository

/**
 * Use case for handling user login.
 * 
 * Responsibilities:
 * 1. Trim email and password inputs
 * 2. Validate email format
 * 3. Validate password strength
 * 4. Hash password with MD5
 * 5. Call repository to authenticate
 * 
 * @property authRepository Repository for authentication operations
 * @property validateEmail Use case for email validation
 * @property validatePassword Use case for password validation
 */
class LoginUseCase(
    private val authRepository: AuthRepository,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePassword: ValidatePasswordUseCase
) {
    /**
     * Authenticates a user with email and password.
     * 
     * @param email User's email address
     * @param password User's password (will be hashed with MD5)
     * @return Result containing User on success, or error on failure
     */
    suspend operator fun invoke(email: String, password: String): Result<User> {
        // Step 1: Trim inputs
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        
        // Step 2: Validate email
        when (val emailValidation = validateEmail(trimmedEmail)) {
            is Result.Error -> return emailValidation
            else -> { /* Continue */ }
        }
        
        // Step 3: Validate password
        when (val passwordValidation = validatePassword(trimmedPassword)) {
            is Result.Error -> return passwordValidation
            else -> { /* Continue */ }
        }
        
        // Step 4: Hash password with MD5
        val hashedPassword = HashUtil.md5(trimmedPassword)
        
        // Step 5: Create credentials and call repository
        val credentials = LoginCredentials(
            email = trimmedEmail,
            password = hashedPassword
        )
        
        return authRepository.login(credentials)
    }
}
