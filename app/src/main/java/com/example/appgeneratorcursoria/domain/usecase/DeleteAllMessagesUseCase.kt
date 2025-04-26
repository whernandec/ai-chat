package com.example.appgeneratorcursoria.domain.usecase

import com.example.appgeneratorcursoria.domain.repository.ChatRepository

class DeleteAllMessagesUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke() = repository.deleteAllMessages()
} 