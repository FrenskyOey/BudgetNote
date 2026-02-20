package feature.onboarding.domain.usecase

import core.domain.model.AppException
import core.domain.model.Result
import feature.onboarding.domain.model.ValidationError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * TDD Test Suite for ValidateEmailUseCase
 * 
 * Test Strategy: Validate different email formats and edge cases
 */
class ValidateEmailUseCaseTest {
    
    private val useCase = ValidateEmailUseCase()
    
    @Test
    fun `should return success when email is valid`() {
        // Arrange
        val validEmails = listOf(
            "user@example.com",
            "test.user@domain.co.uk",
            "firstname.lastname@company.org",
            "user+tag@example.com",
            "123@test.com"
        )
        
        // Act & Assert
        validEmails.forEach { email ->
            val result = useCase(email)
            assertTrue(
                result is Result.Success,
                "Expected success for valid email: $email"
            )
        }
    }
    
    @Test
    fun `should return error when email has no at symbol`() {
        // Arrange
        val invalidEmail = "userexample.com"
        
        // Act
        val result = useCase(invalidEmail)
        
        // Assert
        assertTrue(result is Result.Error)
        val error = (result as Result.Error).exception
        assertTrue(error is AppException.ValidationError)
        assertTrue(error.errorMessage.contains("Invalid email"))
    }
    
    @Test
    fun `should return error when email is malformed`() {
        // Arrange
        val malformedEmails = listOf(
            "atexample.com",          // Missing local part
            "user_at_",               // Missing domain
            "user_at_at_example.com", // Double at
            "user_at_domain",         // Missing TLD
            "user _at_example.com",   // Space in email
            ""                        // Empty string
        )
        
        // Act & Assert
        malformedEmails.forEach { email ->
            val result = useCase(email)
            assertTrue(
                result is Result.Error,
                "Expected error for malformed email: '$email'"
            )
        }
    }
    
    @Test
    fun `should return error when email is empty`() {
        // Arrange
        val emptyEmail = ""
        
        // Act
        val result = useCase(emptyEmail)
        
        // Assert
        assertTrue(result is Result.Error)
        val error = (result as Result.Error).exception
        assertTrue(error is AppException.ValidationError)
    }
    
    @Test
    fun `should return error when email is blank or whitespace`() {
        // Arrange
        val blankEmails = listOf(
            "   ",
            "\t",
            "\n",
            "  \t  \n  "
        )
        
        // Act & Assert
        blankEmails.forEach { email ->
            val result = useCase(email)
            assertTrue(
                result is Result.Error,
                "Expected error for blank email: '$email'"
            )
        }
    }
    
    @Test
    fun `should preserve case sensitivity`() {
        // Arrange - Email should be case-sensitive (no normalization)
        val email1 = "User@Example.Com"
        val email2 = "user@example.com"
        
        // Act
        val result1 = useCase(email1)
        val result2 = useCase(email2)
        
        // Assert - Both should be valid, and no case conversion should happen
        assertTrue(result1 is Result.Success)
        assertTrue(result2 is Result.Success)
        // Note: We don't normalize emails - they remain as-is
    }
}
