package feature.onboarding.domain.usecase

import core.domain.model.AppException
import core.domain.model.Result
import feature.onboarding.domain.model.LoginCredentials
import feature.onboarding.domain.model.User
import feature.onboarding.domain.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * TDD Test Suite for LoginUseCase
 * 
 * Test Strategy:
 * - Test validation logic (delegated to validators)
 * - Test input trimming
 * - Test password hashing
 * - Test repository integration
 * - Test error handling
 */
class LoginUseCaseTest {
    
    @Test
    fun `should return success when credentials are valid`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository()
        val validateEmail = ValidateEmailUseCase()
        val validatePassword = ValidatePasswordUseCase()
        val useCase = LoginUseCase(fakeRepository, validateEmail, validatePassword)
        
        val email = "user@example.com"
        val password = "Test123"
        
        // Act
        val result = useCase(email, password)
        
        // Assert
        assertTrue(result is Result.Success)
        val user = (result as Result.Success).data
        assertEquals("Test User", user.userName)
        assertEquals(1, user.userId)
        assertTrue(user.token.isNotEmpty())
    }
    
    @Test
    fun `should return error when email is invalid`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository()
        val validateEmail = ValidateEmailUseCase()
        val validatePassword = ValidatePasswordUseCase()
        val useCase = LoginUseCase(fakeRepository, validateEmail, validatePassword)
        
        val invalidEmail = "notanemail"
        val password = "Test123"
        
        // Act
        val result = useCase(invalidEmail, password)
        
        // Assert
        assertTrue(result is Result.Error)
        val error = (result as Result.Error).exception
        assertTrue(error is AppException.ValidationError)
        assertTrue(error.errorMessage.contains("email", ignoreCase = true))
    }
    
    @Test
    fun `should return error when password is invalid`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository()
        val validateEmail = ValidateEmailUseCase()
        val validatePassword = ValidatePasswordUseCase()
        val useCase = LoginUseCase(fakeRepository, validateEmail, validatePassword)
        
        val email = "user@example.com"
        val invalidPassword = "short" // Less than 6 chars
        
        // Act
        val result = useCase(email, invalidPassword)
        
        // Assert
        assertTrue(result is Result.Error)
        val error = (result as Result.Error).exception
        assertTrue(error is AppException.ValidationError)
        assertTrue(error.errorMessage.contains("password", ignoreCase = true))
    }
    
    @Test
    fun `should return error when email is empty`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository()
        val validateEmail = ValidateEmailUseCase()
        val validatePassword = ValidatePasswordUseCase()
        val useCase = LoginUseCase(fakeRepository, validateEmail, validatePassword)
        
        val emptyEmail = ""
        val password = "Test123"
        
        // Act
        val result = useCase(emptyEmail, password)
        
        // Assert
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is AppException.ValidationError)
    }
    
    @Test
    fun `should return error when password is empty`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository()
        val validateEmail = ValidateEmailUseCase()
        val validatePassword = ValidatePasswordUseCase()
        val useCase = LoginUseCase(fakeRepository, validateEmail, validatePassword)
        
        val email = "user@example.com"
        val emptyPassword = ""
        
        // Act
        val result = useCase(email, emptyPassword)
        
        // Assert
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).exception is AppException.ValidationError)
    }
    
    @Test
    fun `should trim email and password before validation`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository()
        val validateEmail = ValidateEmailUseCase()
        val validatePassword = ValidatePasswordUseCase()
        val useCase = LoginUseCase(fakeRepository, validateEmail, validatePassword)
        
        val emailWithSpaces = "  user@example.com  "
        val passwordWithSpaces = "  Test123  "
        
        // Act
        val result = useCase(emailWithSpaces, passwordWithSpaces)
        
        // Assert
        assertTrue(result is Result.Success)
        // Verify that the repository received trimmed credentials
        assertEquals("user@example.com", fakeRepository.lastCredentials?.email)
    }
    
    @Test
    fun `should hash password with MD5 before sending to repository`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository()
        val validateEmail = ValidateEmailUseCase()
        val validatePassword = ValidatePasswordUseCase()
        val useCase = LoginUseCase(fakeRepository, validateEmail, validatePassword)
        
        val email = "user@example.com"
        val password = "Test123"
        
        // Act
        val result = useCase(email, password)
        
        // Assert
        assertTrue(result is Result.Success)
        // Verify that password was hashed (should not be the original password)
        assertTrue(fakeRepository.lastCredentials?.password != password)
        // MD5("Test123") = "68eacb97d86f0c4621fa2b0e17cabd8c"
        assertEquals("68eacb97d86f0c4621fa2b0e17cabd8c", fakeRepository.lastCredentials?.password)
    }
    
    @Test
    fun `should return network error when repository returns network error`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository(simulateNetworkError = true)
        val validateEmail = ValidateEmailUseCase()
        val validatePassword = ValidatePasswordUseCase()
        val useCase = LoginUseCase(fakeRepository, validateEmail, validatePassword)
        
        val email = "user@example.com"
        val password = "Test123"
        
        // Act
        val result = useCase(email, password)
        
        // Assert
        assertTrue(result is Result.Error)
        val error = (result as Result.Error).exception
        assertTrue(error is AppException.NetworkError)
    }
    
    @Test
    fun `should return api error when repository returns server error`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository(simulateServerError = true)
        val validateEmail = ValidateEmailUseCase()
        val validatePassword = ValidatePasswordUseCase()
        val useCase = LoginUseCase(fakeRepository, validateEmail, validatePassword)
        
        val email = "user@example.com"
        val password = "Test123"
        
        // Act
        val result = useCase(email, password)
        
        // Assert
        assertTrue(result is Result.Error)
        val error = (result as Result.Error).exception
        assertTrue(error is AppException.ServerError)
    }
    
    // Fake repository for testing
    private class FakeAuthRepository(
        private val simulateNetworkError: Boolean = false,
        private val simulateServerError: Boolean = false
    ) : AuthRepository {
        var lastCredentials: LoginCredentials? = null
        
        override suspend fun login(credentials: LoginCredentials): Result<User> {
            lastCredentials = credentials
            
            return when {
                simulateNetworkError -> Result.Error(
                    AppException.NetworkError("Network connection failed")
                )
                simulateServerError -> Result.Error(
                    AppException.ServerError(500, "Internal server error")
                )
                else -> Result.Success(
                    User(
                        userId = 1,
                        userName = "Test User",
                        token = "fake_token_123"
                    )
                )
            }
        }
        
        override suspend fun logout(): Result<Unit> = Result.Success(Unit)
        override suspend fun getCurrentUser(): Result<User?> = Result.Success(null)
        override suspend fun isLoggedIn(): Boolean = false
    }
    
    // Helper function for running suspending test
    private fun runBlockingTest(block: suspend () -> Unit) {
        kotlinx.coroutines.runBlocking {
            block()
        }
    }
}
