package com.example.appgeneratorcursoria.domain.repository

import com.example.appgeneratorcursoria.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(message: String): ChatMessage
    fun getAllMessages(): Flow<List<ChatMessage>>
    suspend fun insertMessage(message: ChatMessage)
    suspend fun deleteMessage(message: ChatMessage)
    suspend fun deleteAllMessages()
} 