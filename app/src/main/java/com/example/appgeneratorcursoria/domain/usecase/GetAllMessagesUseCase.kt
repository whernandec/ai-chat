package com.example.appgeneratorcursoria.domain.usecase

import com.example.appgeneratorcursoria.domain.model.ChatMessage
import com.example.appgeneratorcursoria.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(): Flow<List<ChatMessage>> = repository.getAllMessages()
} 