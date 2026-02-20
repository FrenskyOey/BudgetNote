package core.data.remote.util

import core.domain.model.AppException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

object ApiErrorHandler {
    fun handleError(throwable: Throwable): AppException {
        return when (throwable) {
            is RedirectResponseException -> AppException.ServerError(throwable.response.status.value, throwable.message)
            is ClientRequestException -> AppException.ServerError(throwable.response.status.value, throwable.message)
            is ServerResponseException -> AppException.ServerError(throwable.response.status.value, throwable.message)
            is IOException -> AppException.NetworkError("Network error: ${throwable.message}")
            is SerializationException -> AppException.ParseError("Data parsing error: ${throwable.message}")
            is AppException -> throwable
            else -> AppException.UnknownError("Unknown error occurred", throwable)
        }
    }

    fun handleHttpError(statusCode: Int, message: String?): AppException {
        return when (statusCode) {
            in 400..499 -> AppException.ServerError(statusCode, message ?: "Client error")
            in 500..599 -> AppException.ServerError(statusCode, message ?: "Server error")
            else -> AppException.UnknownError("HTTP $statusCode: $message")
        }
    }
}
