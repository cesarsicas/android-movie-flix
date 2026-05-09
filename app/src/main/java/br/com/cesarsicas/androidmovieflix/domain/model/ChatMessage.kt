package br.com.cesarsicas.androidmovieflix.domain.model

data class ChatMessage(
    val id: String,
    val role: String,
    val content: String,
    val isStreaming: Boolean = false,
)
