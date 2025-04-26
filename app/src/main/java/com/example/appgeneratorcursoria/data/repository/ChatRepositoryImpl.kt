package com.example.appgeneratorcursoria.data.repository

import com.example.appgeneratorcursoria.data.local.dao.ChatMessageDao
import com.example.appgeneratorcursoria.data.local.entity.ChatMessageEntity
import com.example.appgeneratorcursoria.domain.model.ChatMessage
import com.example.appgeneratorcursoria.domain.model.ChatError
import com.example.appgeneratorcursoria.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch

class ChatRepositoryImpl(
    private val chatMessageDao: ChatMessageDao
) : ChatRepository {
    
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
    
    override suspend fun deleteAllMessages() {
        try {
            chatMessageDao.deleteAllMessages()
        } catch (e: Exception) {
            throw ChatError.DatabaseError("Error deleting all messages: ${e.message}")
        }
    }

    override suspend fun deleteMessage(message: ChatMessage) {
        try {
            chatMessageDao.deleteMessage(
                message = ChatMessageEntity(
                    role = message.role,
                    content = message.content,
                    timestamp = message.timestamp
                )
            )
        } catch (e: Exception) {
            throw ChatError.DatabaseError("Error deleting message: ${e.message}")
        }
    }
} 