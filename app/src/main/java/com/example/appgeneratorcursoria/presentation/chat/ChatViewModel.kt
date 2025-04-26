package com.example.appgeneratorcursoria.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgeneratorcursoria.domain.model.ChatError
import com.example.appgeneratorcursoria.domain.model.ChatMessage
import com.example.appgeneratorcursoria.domain.usecase.DeleteAllMessagesUseCase
import com.example.appgeneratorcursoria.domain.usecase.DeleteMessageUseCase
import com.example.appgeneratorcursoria.domain.usecase.GetAllMessagesUseCase
import com.example.appgeneratorcursoria.domain.usecase.InsertMessageUseCase
import com.example.appgeneratorcursoria.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: ChatError? = null
)

class ChatViewModel(
    private val getAllMessagesUseCase: GetAllMessagesUseCase,
    private val insertMessageUseCase: InsertMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val deleteAllMessagesUseCase: DeleteAllMessagesUseCase,
    private val deleteMessageUseCase: DeleteMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                getAllMessagesUseCase().collect { messages ->
                    _uiState.update { 
                        it.copy(
                            messages = messages,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = ChatError.UnknownError(e.message ?: "Unknown error")
                    )
                }
            }
        }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                // Create and insert user message
                val userMessage = ChatMessage(
                    role = "user",
                    content = content,
                    timestamp = System.currentTimeMillis()
                )
                insertMessageUseCase(userMessage)

                // Get AI response
                val aiMessage = sendMessageUseCase(content)
                insertMessageUseCase(aiMessage)

            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = when (e) {
                        is ChatError -> e
                        else -> ChatError.UnknownError(e.message ?: "Unknown error")
                    })
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteMessage(message: ChatMessage) {
        viewModelScope.launch {
            try {
                deleteMessageUseCase(message)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = ChatError.UnknownError(e.message ?: "Failed to delete message"))
                }
            }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                deleteAllMessagesUseCase()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = ChatError.UnknownError(e.message ?: "Unknown error"))
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}