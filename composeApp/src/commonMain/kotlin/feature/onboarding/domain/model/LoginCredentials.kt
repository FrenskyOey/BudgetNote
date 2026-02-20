package feature.onboarding.domain.model

/**
 * Encapsulates login input data with validation.
 * 
 * @property email User's email address (case-sensitive)
 * @property password User's password (should be hashed before sending to backend)
 */
data class LoginCredentials(
    val email: String,
    val password: String
)
