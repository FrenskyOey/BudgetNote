package feature.onboarding.domain.model

/**
 * Represents validation errors for user inputs in the onboarding feature.
 */
sealed class ValidationError {
    /**
     * Email format is invalid (missing @, malformed structure, etc.)
     */
    data object InvalidEmail : ValidationError()
    
    /**
     * Password is not alphanumeric or less than 6 characters
     */
    data object InvalidPassword : ValidationError()
    
    /**
     * Email field is empty
     */
    data object EmptyEmail : ValidationError()
    
    /**
     * Password field is empty
     */
    data object EmptyPassword : ValidationError()
}
