package core.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseListResponse<T>(
    @SerialName("data") val data: List<T>? = null,
    @SerialName("is_success") val isSuccess: Boolean,
    @SerialName("error_message") val errorMessage: String? = null,
    @SerialName("pagination") val pagination: Pagination? = null
)

@Serializable
data class Pagination(
    @SerialName("key_id") val nextKeyId: Int?,
    @SerialName("has_next") val hasNext: Boolean
)
