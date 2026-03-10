package feature.onboarding.domain.usecase

import core.domain.model.Result
import feature.onboarding.domain.model.LoginCredentials
import feature.onboarding.domain.model.User
import feature.onboarding.domain.repository.AuthRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * TDD Test Suite for GetCurrentUserUseCase
 */
class GetCurrentUserUseCaseTest {
    
    @Test
    fun `should return cached user when logged in`() = runBlockingTest {
        // Arrange
        val expectedUser = User(1, "Test User", "token123")
        val fakeRepository = FakeAuthRepository(currentUser = expectedUser)
        val useCase = GetCurrentUserUseCase(fakeRepository)
        
        // Act
        val result = useCase()
        
        // Assert
        assertTrue(result is Result.Success)
        val user = (result as Result.Success).data
        assertEquals(expectedUser, user)
    }
    
    @Test
    fun `should return null when not logged in`() = runBlockingTest {
        // Arrange
        val fakeRepository = FakeAuthRepository(currentUser = null)
        val useCase = GetCurrentUserUseCase(fakeRepository)
        
        // Act
        val result = useCase()
        
        // Assert
        assertTrue(result is Result.Success)
        val user = (result as Result.Success).data
        assertNull(user)
    }
    
    // Fake repository for testing
    private class FakeAuthRepository(
        private val currentUser: User? = null
    ) : AuthRepository {
        override suspend fun login(credentials: LoginCredentials): Result<User> {
            return Result.Success(User(1, "Test", "token"))
        }
        
        override suspend fun logout(): Result<Unit> = Result.Success(Unit)
        
        override suspend fun getCurrentUser(): Result<User?> {
            return Result.Success(currentUser)
        }
        
        override suspend fun isLoggedIn(): Boolean = currentUser != null
        
        override suspend fun refreshToken(failedAccessToken: String?): Result<String> = Result.Success("token")
        override suspend fun loginWithGoogle(idToken: String, accessToken: String): Result<User> = Result.Success(User(1, "test", "token"))
    }
    
    // Helper function for running suspending test
    private fun runBlockingTest(block: suspend () -> Unit) {
        kotlinx.coroutines.runBlocking {
            block()
        }
    }
}
