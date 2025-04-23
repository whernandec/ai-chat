package com.example.appgeneratorcursoria.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appgeneratorcursoria.data.remote.DeepseekApi
import com.example.appgeneratorcursoria.data.remote.DeepseekRequest
import com.example.appgeneratorcursoria.data.remote.Message
import com.example.appgeneratorcursoria.domain.model.ChatMessage
import com.example.appgeneratorcursoria.domain.usecase.DeleteAllMessagesUseCase
import com.example.appgeneratorcursoria.domain.usecase.GetAllMessagesUseCase
import com.example.appgeneratorcursoria.domain.usecase.InsertMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val insertMessageUseCase: InsertMessageUseCase,
    private val deleteAllMessagesUseCase: DeleteAllMessagesUseCase,
    private val getAllMessagesUseCase: GetAllMessagesUseCase,
    private val deepseekApi: DeepseekApi
) : ViewModel() {
    
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadMessages()
    }
    
    private fun loadMessages() {
        viewModelScope.launch {
            getAllMessagesUseCase().collect { messages ->
                _messages.value = messages
            }
        }
    }
    
    fun sendMessage(message: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Insert user message
                val userMessage = ChatMessage(
                    "User",
                    content = message,
                    timestamp = System.currentTimeMillis()
                )
                insertMessageUseCase(userMessage)

                // Prepare the request
                val request = DeepseekRequest(
                    messages = _messages.value.map {
                        Message(
                            role = it.role,
                            content = it.content
                        )
                    }
                )

                val response = deepseekApi.sendMessage(
                    apiKey = "Bearer sk-edcbbea0719f4fcfbe67c49ddbaf62bb",
                    request = request
                )

                // Add AI response to the list and persist it
                val aiMessage = ChatMessage(
                    "Assistant",
                    content = response.choices.first().message.content,
                    timestamp = System.currentTimeMillis()
                )
                insertMessageUseCase(aiMessage)

            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearChat() {
        viewModelScope.launch {
            deleteAllMessagesUseCase()
        }
    }

    fun clearError() {
        _error.value = null
    }
} 