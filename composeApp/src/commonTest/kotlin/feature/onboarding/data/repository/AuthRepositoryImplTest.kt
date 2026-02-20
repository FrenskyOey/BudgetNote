package feature.onboarding.data.repository

import core.domain.model.AppException
import core.domain.model.Result
import feature.onboarding.data.datasource.AuthDataSource
import feature.onboarding.data.model.response.LoginResponse
import feature.onboarding.data.model.response.UserData
import feature.onboarding.data.model.entity.UserEntity
import feature.onboarding.data.model.request.LoginRequest
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import feature.onboarding.domain.model.LoginCredentials

// Minimal Fake implementations for TDD
class FakeAuthRemoteDataSource : AuthDataSource.Remote {
    var response: LoginResponse? = null
    var thrownException: Exception? = null
    var lastLoginRequest: LoginRequest? = null

    override suspend fun login(request: LoginRequest): LoginResponse {
        lastLoginRequest = request
        thrownException?.let { throw it }
        return response ?: throw IllegalStateException("Response not set")
    }
}

class FakeAuthLocalDataSource : AuthDataSource.Local {
    var savedUser: UserEntity? = null
    var token: String? = null

    override suspend fun saveUser(userEntity: UserEntity) {
        savedUser = userEntity
        token = userEntity.token
    }

    override suspend fun getUser(): UserEntity? {
        return savedUser
    }

    override suspend fun clearUser() {
        savedUser = null
        token = null
    }

    override suspend fun getToken(): String? {
        return token
    }

    override suspend fun clearToken() {
        token = null
    }

    override suspend fun saveToken(token: String) {
       this.token = token
    }
}

class AuthRepositoryImplTest {

    private val remoteDataSource = FakeAuthRemoteDataSource()
    private val localDataSource = FakeAuthLocalDataSource()
    private val repository = AuthRepositoryImpl(remoteDataSource, localDataSource)

    @Test
    fun `login success saves user and returns success`() = runTest {
        // Repository expects password to already be hashed (done by LoginUseCase)
        val hashedPassword = "5f4dcc3b5aa765d61d8327deb882cf99" // MD5 of "password"
        val credentials = LoginCredentials("test@example.com", hashedPassword)
        val userData = UserData("test@example.com", 123, "token")
        remoteDataSource.response = LoginResponse(userData, true)

        val result = repository.login(credentials)

        assertIs<Result.Success<*>>(result)
        val user = (result as Result.Success).data
        assertEquals("test@example.com", user.userName)
        assertEquals("test@example.com", localDataSource.savedUser?.userName)
        assertEquals(hashedPassword, remoteDataSource.lastLoginRequest?.password)
    }

    @Test
    fun `login failure returns Error`() = runTest {
        val hashedPassword = "5f4dcc3b5aa765d61d8327deb882cf99" // MD5 of "password"
        val credentials = LoginCredentials("test@example.com", hashedPassword)
        remoteDataSource.response = LoginResponse(null, false, "Invalid credentials")

        val result = repository.login(credentials)

        assertIs<Result.Error>(result)
        val exception = (result as Result.Error).exception
        assertIs<AppException.AuthException>(exception)
        assertEquals("Invalid credentials", (exception as AppException.AuthException).errorMessage)
    }

    @Test
    fun `logout clears local data`() = runTest {
        localDataSource.savedUser = UserEntity(1, "user", "token")
        
        val result = repository.logout()

        assertIs<Result.Success<*>>(result)
        assertEquals(null, localDataSource.savedUser)
    }

    @Test
    fun `isLoggedIn returns true when token exists`() = runTest {
        localDataSource.token = "token"
        assertTrue(repository.isLoggedIn())
    }
    
    @Test
    fun `isLoggedIn returns false when token missing`() = runTest {
        localDataSource.token = null
        assertFalse(repository.isLoggedIn())
    }
}
