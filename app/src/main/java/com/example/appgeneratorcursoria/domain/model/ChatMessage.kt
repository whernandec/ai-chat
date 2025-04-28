package com.example.appgeneratorcursoria.domain.model

data class ChatMessage(
    val role: String,
    val content: String,
    val timestamp: Long
) {
    // Builder interno
    class Builder {
        private var role: String = ""
        private var content: String = ""
        private var timestamp: Long = System.currentTimeMillis()

        fun role(role: String) = apply { this.role = role }
        fun content(content: String) = apply { this.content = content }
        fun timestamp(timestamp: Long) = apply { this.timestamp = timestamp }

        fun build(): ChatMessage {
            return ChatMessage(
                role = role,
                content = content,
                timestamp = timestamp
            )
        }
    }
}