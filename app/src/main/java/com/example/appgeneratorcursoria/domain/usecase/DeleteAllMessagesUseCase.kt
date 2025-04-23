package com.example.appgeneratorcursoria.domain.usecase

import com.example.appgeneratorcursoria.domain.repository.ChatRepository
import javax.inject.Inject

class DeleteAllMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllMessages()
    }
} 