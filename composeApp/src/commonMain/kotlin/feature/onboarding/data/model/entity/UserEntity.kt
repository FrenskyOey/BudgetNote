package feature.onboarding.data.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val userId: Int,
    val userName: String,
    val token: String
)
