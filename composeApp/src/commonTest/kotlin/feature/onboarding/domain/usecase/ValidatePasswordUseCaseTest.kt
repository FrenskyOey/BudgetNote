package feature.onboarding.domain.usecase

import core.domain.model.AppException
import core.domain.model.Result
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * TDD Test Suite for ValidatePasswordUseCase
 * 
 * Test Strategy: Validate password strength and format requirements
 * Requirements:
 * - Minimum 6 characters
 * - Must contain at least one letter (a-z, A-Z)
 * - Must contain at least one number (0-9)
 */
class ValidatePasswordUseCaseTest {
    
    private val useCase = ValidatePasswordUseCase()
    
    @Test
    fun `should return success when password is valid alphanumeric with 6+ characters`() {
        // Arrange
        val validPasswords = listOf(
            "abc123",          // Exactly 6 chars
            "Test1234",        // Mixed case with numbers
            "password123",     // Lowercase + numbers
            "SECURE999",       // Uppercase + numbers
            "a1b2c3d4e5",      // Alternating letters and numbers
            "MyP@ssw0rd"       // Contains special chars (allowed but not required)
        )
        
        // Act & Assert
        validPasswords.forEach { password ->
            val result = useCase(password)
            assertTrue(
                result is Result.Success,
                "Expected success for valid password: $password"
            )
        }
    }
    
    @Test
    fun `should return error when password is less than 6 characters`() {
        // Arrange
        val shortPasswords = listOf(
            "ab1",        // 3 chars
            "test1",      // 5 chars
            "a1",         // 2 chars
            "12a"         // 3 chars
        )
        
        // Act & Assert
        shortPasswords.forEach { password ->
            val result = useCase(password)
            assertTrue(
                result is Result.Error,
                "Expected error for short password: $password"
            )
            val error = (result as Result.Error).exception
            assertTrue(error is AppException.ValidationError)
            assertTrue(
                error.errorMessage.contains("6 characters") || 
                error.errorMessage.contains("minimum"),
                "Error message should mention minimum length requirement"
            )
        }
    }
    
    @Test
    fun `should return error when password is empty`() {
        // Arrange
        val emptyPassword = ""
        
        // Act
        val result = useCase(emptyPassword)
        
        // Assert
        assertTrue(result is Result.Error)
        val error = (result as Result.Error).exception
        assertTrue(error is AppException.ValidationError)
        assertTrue(
            error.errorMessage.contains("empty") || 
            error.errorMessage.contains("required"),
            "Error message should indicate password is required"
        )
    }
    
    @Test
    fun `should return error when password is blank or whitespace`() {
        // Arrange
        val blankPasswords = listOf(
            "   ",
            "\t\t",
            "\n",
            "  \t  "
        )
        
        // Act & Assert
        blankPasswords.forEach { password ->
            val result = useCase(password)
            assertTrue(
                result is Result.Error,
                "Expected error for blank password"
            )
        }
    }
    
}
