package com.example.appgeneratorcursoria.domain.usecase

import com.example.appgeneratorcursoria.data.remote.DeepseekApi
import com.example.appgeneratorcursoria.data.remote.DeepseekRequest
import com.example.appgeneratorcursoria.data.remote.Message
import com.example.appgeneratorcursoria.domain.model.ChatError
import com.example.appgeneratorcursoria.domain.model.ChatMessage
import com.example.appgeneratorcursoria.domain.repository.ChatRepository
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException
import java.io.IOException

class SendMessageUseCase : KoinComponent {
    private val deepseekApi: DeepseekApi by inject()
    private val chatRepository: ChatRepository by inject()
    private val apiKey: String by inject()

    companion object {
        private const val ROLE_USER = "user"
        private const val ROLE_ASSISTANT = "assistant"
        private const val MAX_CONTEXT_MESSAGES = 10 // Limitar el número de mensajes de contexto
    }

    suspend operator fun invoke(message: String): ChatMessage {
        if (message.isBlank()) {
            throw ChatError.ValidationError("Message cannot be empty")
        }

        try {
            // Get recent messages for context
            val previousMessages = chatRepository.getAllMessages()
                .firstOrNull()
                ?.takeLast(MAX_CONTEXT_MESSAGES)
                ?.map { chatMessage ->
                    Message(
                        role = chatMessage.role,
                        content = chatMessage.content
                    )
                } ?: emptyList()

            // Prepare the request with context
            val request = DeepseekRequest(
                messages = previousMessages + Message(
                    role = ROLE_USER,
                    content = message
                )
            )

            // Send request to API
            val response = try {
                deepseekApi.sendMessage(
                    apiKey = "Bearer $apiKey",
                    request = request
                )
            } catch (e: HttpException) {
                throw when (e.code()) {
                    401 -> ChatError.AuthenticationError("Invalid API key")
                    429 -> ChatError.RateLimitError("Too many requests")
                    in 500..599 -> ChatError.ServerError("Server error: ${e.message()}")
                    else -> ChatError.NetworkError("HTTP error: ${e.message()}")
                }
            } catch (e: IOException) {
                throw ChatError.NetworkError("Network error: ${e.message}")
            }

            // Validate response
            val aiMessage = response.choices.firstOrNull()?.message?.content
                ?: throw ChatError.ServerError("Empty response from server")

            // Create and return AI response
            return ChatMessage(
                role = ROLE_ASSISTANT,
                content = aiMessage,
                timestamp = System.currentTimeMillis()
            )
        } catch (e: Exception) {
            throw when (e) {
                is ChatError -> e
                else -> ChatError.UnknownError("Unexpected error: ${e.message}")
            }
        }
    }
} 