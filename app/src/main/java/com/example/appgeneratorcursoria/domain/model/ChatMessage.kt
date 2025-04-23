package com.example.appgeneratorcursoria.domain.model

data class ChatMessage(
    val role: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
) 