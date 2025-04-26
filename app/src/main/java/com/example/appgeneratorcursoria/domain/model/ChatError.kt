package com.example.appgeneratorcursoria.domain.model

sealed class ChatError : Exception() {
    data class NetworkError(override val message: String) : ChatError()
    data class DatabaseError(override val message: String) : ChatError()
    data class UnknownError(override val message: String) : ChatError()
    data class ValidationError(override val message: String) : ChatError()
    data class AuthenticationError(override val message: String) : ChatError()
    data class RateLimitError(override val message: String) : ChatError()
    data class ServerError(override val message: String) : ChatError()

    override fun toString(): String = when (this) {
        is NetworkError -> "NetworkError: $message"
        is DatabaseError -> "DatabaseError: $message"
        is UnknownError -> "UnknownError: $message"
        is ValidationError -> "ValidationError: $message"
        is AuthenticationError -> "AuthenticationError: $message"
        is RateLimitError -> "RateLimitError: $message"
        is ServerError -> "ServerError: $message"
    }
} 