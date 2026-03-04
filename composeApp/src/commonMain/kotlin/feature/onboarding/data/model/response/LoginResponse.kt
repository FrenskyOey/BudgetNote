package feature.onboarding.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("data") val data: UserData?,
    @SerialName("is_success") val isSuccess: Boolean,
    @SerialName("error_message") val errorMessage: String? = null
)

@Serializable
data class UserData(
    @SerialName("user_name") val userName: String,
    @SerialName("user_id") val userId: Int,
    @SerialName("user_email") val email: String,
    @SerialName("user_phone") val phone: String,
    @SerialName("token") val token: String,
    @SerialName("refresh_token") val refreshToken: String
)
