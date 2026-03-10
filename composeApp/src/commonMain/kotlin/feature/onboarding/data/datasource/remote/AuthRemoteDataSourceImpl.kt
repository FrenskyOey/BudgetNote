package feature.onboarding.data.datasource.remote

import core.domain.model.AppException
import core.util.log.LogHelper
import feature.onboarding.data.datasource.AuthDataSource
import feature.onboarding.data.model.request.LoginRequest
import feature.onboarding.data.model.response.LoginResponse
import feature.onboarding.data.model.response.UserData
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken

class AuthRemoteDataSourceImpl(
    private val supabaseClient: SupabaseClient
) : AuthDataSource.Remote {
    override suspend fun login(request: LoginRequest): LoginResponse {
        return try {
            supabaseClient.auth.signInWith(Email) {
                email = request.userName
                password = request.password
            }
            
            val session = supabaseClient.auth.currentSessionOrNull()
                ?: throw AppException.AuthException("Failed to retrieve session after login")
                
            val user = supabaseClient.auth.currentUserOrNull()
                ?: throw AppException.AuthException("Failed to retrieve user after login")
                
            val userData = UserData(
                userName = user.userMetadata?.get("full_name")?.toString()?.replace("\"", "") ?: user.email ?: "User",
                userId = user.id.hashCode(), // Generate an Int since user.id is UUID string
                email = user.email ?: "",
                phone = user.phone ?: "",
                token = session.accessToken,
                refreshToken = session.refreshToken ?: ""
            )
            
            LoginResponse(
                data = userData,
                isSuccess = true
            )
        } catch (e: AuthRestException) {
            LogHelper().error(e.message ?: "SUPABASE Error Auth",e, "SUPABASE")
            throw AppException.AuthException(e.errorDescription ?: "Unknown login error")
        }catch (e : Exception){
            LogHelper().error(e.message ?: "Exception",e, "SUPABASE")
            throw AppException.AuthException(e.message ?: "Unknown login error")
        }
    }

    override suspend fun refreshToken(refreshToken: String): LoginResponse {
        return try {
            // With Supabase, we can refresh the session using refreshCurrentSession() 
            // if the token is already in local storage used by Auth. But to be safe explicitly:
            // Since we use custom token handling, we might just call refreshCurrentSession()
            // Assume the SupabaseClient auth plugin has the token or we supply it. 
            // In jan-tennert/supabase auth, refreshCurrentSession() refreshes it.
            supabaseClient.auth.refreshCurrentSession()
            
            val session = supabaseClient.auth.currentSessionOrNull()
                ?: throw AppException.AuthException("Failed to retrieve session after refresh")
                
            val user = supabaseClient.auth.currentUserOrNull()
                ?: throw AppException.AuthException("Failed to retrieve user after refresh")
                
            val userData = UserData(
                userName = user.userMetadata?.get("full_name")?.toString()?.replace("\"", "") ?: user.email ?: "User",
                userId = user.id.hashCode(),
                email = user.email ?: "",
                phone = user.phone ?: "",
                token = session.accessToken,
                refreshToken = session.refreshToken ?: ""
            )
            
            LoginResponse(
                data = userData,
                isSuccess = true
            )
        } catch (e: Exception) {
            LogHelper().error(e.message ?: "SUPABASE Error Auth",e, "SUPABASE")
            throw AppException.AuthException(e.message ?: "Unknown refresh token error")
        }
    }

    override suspend fun loginWithGoogle(idToken: String, accessToken: String): LoginResponse {
        return try {
            supabaseClient.auth.signInWith(IDToken) {
                this.idToken = idToken
                this.accessToken = accessToken
                this.provider = Google
            }

            val session = supabaseClient.auth.currentSessionOrNull()
                ?: throw AppException.AuthException("Failed to retrieve session after Google sign-in")

            val user = supabaseClient.auth.currentUserOrNull()
                ?: throw AppException.AuthException("Failed to retrieve user after Google sign-in")

            val userData = UserData(
                userName = user.userMetadata?.get("full_name")?.toString()?.replace("\"", "")
                    ?: user.userMetadata?.get("name")?.toString()?.replace("\"", "")
                    ?: user.email
                    ?: "User",
                userId = user.id.hashCode(),
                email = user.email ?: "",
                phone = user.phone ?: "",
                token = session.accessToken,
                refreshToken = session.refreshToken ?: ""
            )

            LoginResponse(
                data = userData,
                isSuccess = true
            )
        } catch (e: AuthRestException) {
            LogHelper().error(e.message ?: "SUPABASE Google SSO Error", e, "SUPABASE")
            throw AppException.AuthException(e.errorDescription ?: "Google sign-in failed")
        } catch (e: Exception) {
            LogHelper().error(e.message ?: "Exception", e, "SUPABASE")
            throw AppException.AuthException(e.message ?: "Google sign-in failed")
        }
    }
}
