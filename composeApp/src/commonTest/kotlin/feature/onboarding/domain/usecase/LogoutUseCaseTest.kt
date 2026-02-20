package feature.onboarding.domain.usecase

import core.domain.model.Result
import feature.onboarding.domain.model.LoginCredentials
import feature.onboarding.domain.model.User
import feature.onboarding.domain.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * TDD Test Suite for LogoutUseCase
 */
class LogoutUseCaseTest {
    
    @Test
    fun `should call repository logout`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository()
        val useCase = LogoutUseCase(fakeRepository)
        
        // Act
        val result = useCase()
        
        // Assert
        assertTrue(result is Result.Success)
        assertTrue(fakeRepository.logoutCalled)
    }
    
    @Test
    fun `should return success when logout succeeds`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository()
        val useCase = LogoutUseCase(fakeRepository)
        
        // Act
        val result = useCase()
        
        // Assert
        assertTrue(result is Result.Success)
    }
    
    // Fake repository for testing
    private class FakeAuthRepository : AuthRepository {
        var logoutCalled = false
        
        override suspend fun login(credentials: LoginCredentials): Result<User> {
            return Result.Success(User(1, "Test", "token"))
        }
        
        override suspend fun logout(): Result<Unit> {
            logoutCalled = true
            return Result.Success(Unit)
        }
        
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
