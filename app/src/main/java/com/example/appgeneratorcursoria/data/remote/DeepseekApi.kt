package com.example.appgeneratorcursoria.data.remote

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DeepseekApi {
    @POST("v1/chat/completions")
    suspend fun sendMessage(
        @Header("Authorization") apiKey: String,
        @Body request: DeepseekRequest
    ): DeepseekResponse
}

data class DeepseekRequest(
    val messages: List<Message>,
    val model: String = "deepseek-chat",
    val temperature: Double = 0.7
)

data class Message(
    val role: String,
    val content: String
)

data class DeepseekResponse(
    val id: String,
    val choices: List<Choice>,
    val created: Long,
    val model: String,
    val usage: Usage
)

data class Choice(
    val index: Int,
    val message: Message,
    val finishReason: String
)

data class Usage(
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int
) 