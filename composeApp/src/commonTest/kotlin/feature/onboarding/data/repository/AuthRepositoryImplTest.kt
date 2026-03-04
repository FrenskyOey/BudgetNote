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

    override suspend fun refreshToken(refreshToken: String): LoginResponse {
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

    var refreshToken: String? = null

    override suspend fun getRefreshToken(): String? {
        return refreshToken
    }

    override suspend fun saveRefreshToken(token: String) {
        this.refreshToken = token
    }

    override suspend fun clearRefreshToken() {
        refreshToken = null
    }
}

class AuthRepositoryImplTest {

    private val remoteDataSource = FakeAuthRemoteDataSource()
    private val localDataSource = FakeAuthLocalDataSource()
    private val repository = AuthRepositoryImpl(remoteDataSource, localDataSource)

    @Test
    fun `login success saves user and returns success`() = runTest {
        val credentials = LoginCredentials("test@example.com", "password")
        val userData = UserData(
            userName = "test@example.com",
            userId = 123,
            email = "test@example.com",
            phone = "+1234567890",
            token = "token",
            refreshToken = "refresh_token"
        )
        remoteDataSource.response = LoginResponse(userData, true)

        val result = repository.login(credentials)

        assertIs<Result.Success<*>>(result)
        val user = (result as Result.Success).data
        assertEquals("test@example.com", user.userName)
        assertEquals("test@example.com", localDataSource.savedUser?.userName)
        assertEquals("password", remoteDataSource.lastLoginRequest?.password)
    }

    @Test
    fun `login failure returns Error`() = runTest {
        val credentials = LoginCredentials("test@example.com", "password")
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

    @Test
    fun `refreshToken success updates local tokens`() = runTest {
        localDataSource.token = "old_token"
        localDataSource.refreshToken = "old_refresh_token"
        localDataSource.savedUser = UserEntity(1, "user", "old_token") // Just mock state
        
        val userData = UserData(
            userName = "test",
            userId = 123,
            email = "test",
            phone = "123",
            token = "new_token",
            refreshToken = "new_refresh_token"
        )
        remoteDataSource.response = LoginResponse(userData, true)

        val result = repository.refreshToken()

        assertIs<Result.Success<*>>(result)
        assertEquals("new_token", localDataSource.token)
        assertEquals("new_refresh_token", localDataSource.refreshToken)
        assertEquals("new_token", (result as Result.Success).data)
    }

    @Test
    fun `refreshToken failure clears local data`() = runTest {
        localDataSource.token = "old_token"
        localDataSource.refreshToken = "old_refresh_token"
        localDataSource.savedUser = UserEntity(1, "user", "old_token")
        
        remoteDataSource.response = LoginResponse(null, false, "Expired refresh token")

        val result = repository.refreshToken()

        assertIs<Result.Error>(result)
        assertEquals(null, localDataSource.token)
        assertEquals(null, localDataSource.refreshToken)
        assertEquals(null, localDataSource.savedUser)
    }

    @Test
    fun `refreshToken exception clears local data`() = runTest {
        localDataSource.token = "old_token"
        localDataSource.refreshToken = "old_refresh_token"
        localDataSource.savedUser = UserEntity(1, "user", "old_token")
        
        remoteDataSource.thrownException = RuntimeException("Network Error")

        val result = repository.refreshToken()

        assertIs<Result.Error>(result)
        assertEquals(null, localDataSource.token)
        assertEquals(null, localDataSource.refreshToken)
        assertEquals(null, localDataSource.savedUser)
    }
}
