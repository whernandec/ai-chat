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
    val model: String = "deepseek-chat",
    val messages: List<Message>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 1000
)

data class Message(
    val role: String,
    val content: String
)

data class DeepseekResponse(
    val id: String,
    val objectType: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>
)

data class Choice(
    val message: Message
) 