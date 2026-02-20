package feature.onboarding.domain.model

/**
 * Represents an authenticated user in the domain layer.
 * 
 * @property userId Unique identifier for the user
 * @property userName Display name of the user
 * @property token Authentication token for API requests
 */
data class User(
    val userId: Int,
    val userName: String,
    val token: String
)
