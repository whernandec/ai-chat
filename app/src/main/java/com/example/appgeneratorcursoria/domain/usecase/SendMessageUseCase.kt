package com.example.appgeneratorcursoria.domain.usecase

import com.example.appgeneratorcursoria.domain.model.ChatMessage
import com.example.appgeneratorcursoria.domain.repository.ChatRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SendMessageUseCase(private val chatRepository: ChatRepository){
    suspend operator fun invoke(message: String): ChatMessage {
        return chatRepository.sendMessage(message)
    }
}



