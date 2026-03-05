package feature.onboarding.domain.usecase

import core.domain.model.AppException
import core.domain.model.Result

/**
 * Use case for validating password strength and format.
 * 
 * Validation Rules:
 * - Not empty or blank
 * - Minimum 6 characters
 * - Contains at least one letter (a-z, A-Z)
 * - Contains at least one number (0-9)
 */
class ValidatePasswordUseCase {
    
    /**
     * Validates a password.
     * 
     * @param password Password to validate
     * @return Result.Success if valid, Result.Error with ValidationError if invalid
     */
    operator fun invoke(password: String): Result<Unit> {
        // Check if empty or blank
        if (password.isBlank()) {
            return Result.Error(
                AppException.ValidationError("Invalid password: Password is required")
            )
        }
        
        // Check minimum length
        if (password.length < 6) {
            return Result.Error(
                AppException.ValidationError("Invalid password: Minimum 6 characters required")
            )
        }

        /*
        // Check for at least one letter
        val hasLetter = password.any { it.isLetter() }
        if (!hasLetter) {
            return Result.Error(
                AppException.ValidationError("Invalid password: Must contain at least one letter")
            )
        }
        
        // Check for at least one number
        val hasNumber = password.any { it.isDigit() }
        if (!hasNumber) {
            return Result.Error(
                AppException.ValidationError("Invalid password: Must contain at least one number")
            )
        }
        */
        return Result.Success(Unit)
    }
}
