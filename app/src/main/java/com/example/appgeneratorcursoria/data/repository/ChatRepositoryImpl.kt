package com.example.appgeneratorcursoria.data.repository

import com.example.appgeneratorcursoria.data.local.dao.ChatMessageDao
import com.example.appgeneratorcursoria.data.local.entity.ChatMessageEntity
import com.example.appgeneratorcursoria.data.remote.DeepseekApi
import com.example.appgeneratorcursoria.data.remote.DeepseekRequest
import com.example.appgeneratorcursoria.data.remote.Message
import com.example.appgeneratorcursoria.domain.model.ChatError
import com.example.appgeneratorcursoria.domain.model.ChatMessage
import com.example.appgeneratorcursoria.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import retrofit2.HttpException
import java.io.IOException

class ChatRepositoryImpl(
    private val chatMessageDao: ChatMessageDao,
    private val deepseekApi: DeepseekApi,
    private val apiKey: String
    ) : ChatRepository {

    companion object {
        private const val ROLE_USER = "user"
        private const val ROLE_ASSISTANT = "assistant"
        private const val MAX_CONTEXT_MESSAGES = 10
    }

    override suspend fun sendMessage(message: String): ChatMessage {
        if (message.isBlank()) {
            throw ChatError.ValidationError("Message cannot be empty")
        }

        try {
            // Get context messages
            val previousMessages = chatMessageDao.getAllMessages()
                .first()
                .takeLast(MAX_CONTEXT_MESSAGES)
                .map { entity ->
                    Message(
                        role = entity.role,
                        content = entity.content
                    )
                }

            // Send to API
            val response = try {
                deepseekApi.sendMessage(
                    apiKey = "Bearer $apiKey",
                    request = DeepseekRequest(
                        messages = previousMessages + Message(
                            role = ROLE_USER,
                            content = message
                        )
                    )
                )
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> throw ChatError.AuthenticationError("Invalid API key")
                    429 -> throw ChatError.RateLimitError("Too many requests")
                    in 500..599 -> throw ChatError.ServerError("Server error: ${e.message()}")
                    else -> throw ChatError.NetworkError("HTTP error: ${e.message()}")
                }
            } catch (e: IOException) {
                throw ChatError.NetworkError("Network error: ${e.message}")
            }

            // Process response
            val aiMessage = response.choices.firstOrNull()?.message?.content
                ?: throw ChatError.ServerError("Empty response from server")

            // Save messages
            val userMessage = ChatMessage.Builder()
                .role(ROLE_USER)
                .content(ROLE_USER)
                .build()

            val assistantMessage = ChatMessage.Builder()
                .role(ROLE_ASSISTANT)
                .content(aiMessage)
                .build()

            insertMessage(userMessage)
            insertMessage(assistantMessage)

            return assistantMessage
        } catch (e: Exception) {
            throw when (e) {
                is ChatError -> e
                else -> ChatError.UnknownError("Unexpected error: ${e.message}")
            }
        }
    }

    override fun getAllMessages(): Flow<List<ChatMessage>> {
        return chatMessageDao.getAllMessages()
            .map { entities ->
                entities.map { entity ->
                    ChatMessage(
                        role = entity.role,
                        content = entity.content,
                        timestamp = entity.timestamp
                    )
                }
            }
            .catch { error ->
                throw ChatError.DatabaseError("Error getting messages: ${error.message}")
            }
    }

    override suspend fun insertMessage(message: ChatMessage) {
        try {
            chatMessageDao.insertMessage(
                ChatMessageEntity(
                    role = message.role,
                    content = message.content,
                    timestamp = message.timestamp
                )
            )
        } catch (e: Exception) {
            throw ChatError.DatabaseError("Error inserting message: ${e.message}")
        }
    }

    override suspend fun deleteMessage(message: ChatMessage) {
        try {
            chatMessageDao.deleteMessage(
                ChatMessageEntity(
                    role = message.role,
                    content = message.content,
                    timestamp = message.timestamp
                )
            )
        } catch (e: Exception) {
            throw ChatError.DatabaseError("Error deleting message: ${e.message}")
        }
    }

    override suspend fun deleteAllMessages() {
        try {
            chatMessageDao.deleteAllMessages()
        } catch (e: Exception) {
            throw ChatError.DatabaseError("Error deleting all messages: ${e.message}")
        }
    }
} 