package com.example.appgeneratorcursoria.domain.usecase

import com.example.appgeneratorcursoria.domain.model.ChatMessage
import com.example.appgeneratorcursoria.domain.repository.ChatRepository

class InsertMessageUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(message: ChatMessage) = repository.insertMessage(message)
} 