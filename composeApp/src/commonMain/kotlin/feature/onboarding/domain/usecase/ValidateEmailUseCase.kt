package feature.onboarding.domain.usecase

import core.domain.model.AppException
import core.domain.model.Result

/**
 * Use case for validating email format.
 * 
 * Validation Rules:
 * - Not empty or blank
 * - Contains exactly one @ symbol
 * - Has valid structure (local@domain)
 * - Case-sensitive (no normalization)
 */
class ValidateEmailUseCase {
    
    /**
     * Validates an email address.
     * 
     * @param email Email address to validate
     * @return Result.Success if valid, Result.Error with ValidationError if invalid
     */
    operator fun invoke(email: String): Result<Unit> {
        // Check if empty or blank
        if (email.isBlank()) {
            return Result.Error(
                AppException.ValidationError("Invalid email: Email cannot be empty")
            )
        }
        
        // Check if contains exactly one @
        val atCount = email.count { it == '@' }
        if (atCount != 1) {
            return Result.Error(
                AppException.ValidationError("Invalid email: Must contain exactly one @ symbol")
            )
        }
        
        // Split email into local and domain parts
        val parts = email.split("@")
        val localPart = parts[0]
        val domainPart = parts[1]
        
        // Validate local part (before @)
        if (localPart.isEmpty() || localPart.contains(" ")) {
            return Result.Error(
                AppException.ValidationError("Invalid email: Invalid local part")
            )
        }
        
        // Validate domain part (after @)
        if (domainPart.isEmpty() || !domainPart.contains(".") || domainPart.contains(" ")) {
            return Result.Error(
                AppException.ValidationError("Invalid email: Invalid domain")
            )
        }
        
        // Additional check: domain must have at least one character before and after the dot
        val domainParts = domainPart.split(".")
        if (domainParts.any { it.isEmpty() }) {
            return Result.Error(
                AppException.ValidationError("Invalid email: Invalid domain structure")
            )
        }
        
        return Result.Success(Unit)
    }
}
