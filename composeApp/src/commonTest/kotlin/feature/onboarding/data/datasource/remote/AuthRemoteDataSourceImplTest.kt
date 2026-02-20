import core.domain.model.AppException
import feature.onboarding.data.api.AuthApiService
import feature.onboarding.data.datasource.remote.AuthRemoteDataSourceImpl
import feature.onboarding.data.model.request.LoginRequest
import feature.onboarding.data.model.response.LoginResponse
import feature.onboarding.data.model.response.UserData
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FakeAuthApiService : AuthApiService {
    var response: LoginResponse? = null
    var thrownException: Exception? = null

    override suspend fun login(request: LoginRequest): LoginResponse {
        thrownException?.let { throw it }
        return response ?: throw IllegalStateException("Response not set")
    }
}

class AuthRemoteDataSourceImplTest {

    @Test
    fun `login success returns LoginResponse`() = runTest {
        val fakeService = FakeAuthApiService()
        val userData = UserData("test@example.com", 123, "valid_token")
        fakeService.response = LoginResponse(userData, true)
        
        val dataSource = AuthRemoteDataSourceImpl(fakeService)
        
        val result = dataSource.login(LoginRequest("test@example.com", "password"))

        assertEquals(true, result.isSuccess)
        assertEquals("test@example.com", result.data?.userName)
    }

    @Test
    fun `login failure throws AuthException`() = runTest {
        val fakeService = FakeAuthApiService()
        fakeService.response = LoginResponse(null, false, "Invalid credentials")
        
        val dataSource = AuthRemoteDataSourceImpl(fakeService)

        val exception = assertFailsWith<AppException.AuthException> {
            dataSource.login(LoginRequest("test@example.com", "wrong_password"))
        }

        assertEquals("Invalid credentials", exception.errorMessage)
    }
}
