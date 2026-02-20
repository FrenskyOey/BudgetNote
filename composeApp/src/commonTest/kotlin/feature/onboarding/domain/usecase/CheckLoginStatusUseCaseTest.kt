package feature.onboarding.domain.usecase

import core.domain.model.Result
import feature.onboarding.domain.model.LoginCredentials
import feature.onboarding.domain.model.User
import feature.onboarding.domain.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * TDD Test Suite for CheckLoginStatusUseCase
 */
class CheckLoginStatusUseCaseTest {
    
    @Test
    fun `should return true when token exists`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository(isLoggedIn = true)
        val useCase = CheckLoginStatusUseCase(fakeRepository)
        
        // Act
        val result = useCase()
        
        // Assert
        assertTrue(result)
    }
    
    @Test
    fun `should return false when no token`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository(isLoggedIn = false)
        val useCase = CheckLoginStatusUseCase(fakeRepository)
        
        // Act
        val result = useCase()
        
        // Assert
        assertFalse(result)
    }
    
    // Fake repository for testing
    private class FakeAuthRepository(
        private val isLoggedIn: Boolean = false
    ) : AuthRepository {
        override suspend fun login(credentials: LoginCredentials): Result<User> {
            return Result.Success(User(1, "Test", "token"))
        }
        
        override suspend fun logout(): Result<Unit> = Result.Success(Unit)
        override suspend fun getCurrentUser(): Result<User?> = Result.Success(null)
        
        override suspend fun isLoggedIn(): Boolean = isLoggedIn
    }
    
    // Helper function for running suspending test
    private fun runBlockingTest(block: suspend () -> Unit) {
        kotlinx.coroutines.runBlocking {
            block()
        }
    }
}
