package com.example.appgeneratorcursoria.domain.repository

import com.example.appgeneratorcursoria.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getAllMessages(): Flow<List<ChatMessage>>
    suspend fun insertMessage(message: ChatMessage)
    suspend fun deleteAllMessages()
    suspend fun deleteMessage(message: ChatMessage)
} 