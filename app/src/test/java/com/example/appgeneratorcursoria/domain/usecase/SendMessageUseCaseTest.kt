package com.example.appgeneratorcursoria.domain.usecase

import com.example.appgeneratorcursoria.domain.model.ChatError
import com.example.appgeneratorcursoria.domain.model.ChatMessage
import com.example.appgeneratorcursoria.domain.repository.ChatRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertFailsWith


@ExperimentalCoroutinesApi
class SendMessageUseCaseTest {

    private lateinit var sendMessageUseCase: SendMessageUseCase

    @Mock
    private lateinit var chatRepository: ChatRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sendMessageUseCase = SendMessageUseCase(chatRepository)
    }

    @Test
    fun `invoke returns ChatMessage when repository succeeds`(): Unit = runBlocking {
        // Arrange mandamus mensaje a la AI
        val inputMessage = "Hola, AI!"

        val expectedChatMessage = ChatMessage.Builder()
            .role("assistant")
            .content("que es una prueba unitaria en android !")
            .build()


        `when`(chatRepository.sendMessage(inputMessage)).thenReturn(expectedChatMessage)

        // Act
        val result = sendMessageUseCase.invoke(inputMessage)

        // Assert
        assertEquals(expectedChatMessage, result)
        verify(chatRepository).sendMessage(inputMessage)
    }

    @Test
    fun `invoke throws ValidationError when message is blank`(): Unit = runBlocking {
        // Arrange
        val blankMessage = ""

        `when`(chatRepository.sendMessage(blankMessage))
            .thenThrow(ChatError.ValidationError("Message cannot be empty"))

        // Act & Assert
        assertFailsWith<ChatError.ValidationError> {
            sendMessageUseCase.invoke(blankMessage)
        }
        verify(chatRepository).sendMessage(blankMessage)
    }

    @Test
    fun `invoke throws ServerError when repository fails`(): Unit = runBlocking {
        // Arrange
        val inputMessage = "Server?"
        `when`(chatRepository.sendMessage(inputMessage))
            .thenThrow(ChatError.ServerError("Server is down"))

        // Act & Assert
        assertFailsWith<ChatError.ServerError> {
            sendMessageUseCase.invoke(inputMessage)
        }
        verify(chatRepository).sendMessage(inputMessage)
    }
}
