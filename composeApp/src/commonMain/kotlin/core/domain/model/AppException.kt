package core.domain.model

sealed class AppException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    // Network errors
    data class NetworkError(val errorMessage: String? = null) : AppException(errorMessage)
    data class ServerError(val code: Int, val errorMessage: String? = null) : AppException(errorMessage)
    data class TimeoutError(val errorMessage: String? = null) : AppException(errorMessage)
    
    // Data errors
    data class ParseError(val errorMessage: String? = null) : AppException(errorMessage)
    data class CacheError(val errorMessage: String? = null) : AppException(errorMessage)
    
    // Business logic errors
    data class ValidationError(val errorMessage: String) : AppException(errorMessage)
    data class NotFoundError(val errorMessage: String? = null) : AppException(errorMessage)
    data class AuthException(val errorMessage: String) : AppException(errorMessage)
    
    // Unknown
    data class UnknownError(val errorMessage: String? = null, val throwable: Throwable? = null) : AppException(errorMessage, throwable)
}
