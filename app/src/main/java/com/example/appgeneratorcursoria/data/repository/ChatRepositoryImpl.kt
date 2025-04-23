package com.example.appgeneratorcursoria.data.repository

import com.example.appgeneratorcursoria.data.local.dao.ChatMessageDao
import com.example.appgeneratorcursoria.data.local.entity.ChatMessageEntity
import com.example.appgeneratorcursoria.domain.model.ChatMessage
import com.example.appgeneratorcursoria.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val chatMessageDao: ChatMessageDao
) : ChatRepository {
    
    override fun getAllMessages(): Flow<List<ChatMessage>> {
        return chatMessageDao.getAllMessages().map { entities ->
            entities.map { entity ->
                ChatMessage(
                    role = entity.role,
                    content = entity.content,
                    timestamp = entity.timestamp
                )
            }
        }
    }
    
    override suspend fun insertMessage(message: ChatMessage) {
        chatMessageDao.insertMessage(
            ChatMessageEntity(
                role = message.role,
                content = message.content,
                timestamp = message.timestamp
            )
        )
    }
    
    override suspend fun deleteAllMessages() {
        chatMessageDao.deleteAllMessages()
    }
} 