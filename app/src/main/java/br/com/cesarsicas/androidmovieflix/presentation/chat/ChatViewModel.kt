package br.com.cesarsicas.androidmovieflix.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.data.remote.ChatSseClient
import br.com.cesarsicas.androidmovieflix.domain.model.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isStreaming: Boolean = false,
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatSseClient: ChatSseClient,
) : ViewModel() {

    private val _state = MutableStateFlow(ChatUiState())
    val state: StateFlow<ChatUiState> = _state.asStateFlow()

    fun sendMessage(content: String) {
        if (content.isBlank() || _state.value.isStreaming) return

        val userMsg = ChatMessage(id = UUID.randomUUID().toString(), role = "user", content = content)
        val assistantId = UUID.randomUUID().toString()
        val assistantMsg = ChatMessage(id = assistantId, role = "assistant", content = "", isStreaming = true)

        _state.update {
            it.copy(messages = it.messages + userMsg + assistantMsg, isStreaming = true)
        }

        viewModelScope.launch {
            try {
                chatSseClient.sendMessage(content).collect { token ->
                    _state.update { state ->
                        val updated = state.messages.map { msg ->
                            if (msg.id == assistantId) msg.copy(content = msg.content + token)
                            else msg
                        }
                        state.copy(messages = updated)
                    }
                }
            } catch (_: Exception) {
            } finally {
                _state.update { state ->
                    val updated = state.messages.map { msg ->
                        if (msg.id == assistantId) msg.copy(isStreaming = false) else msg
                    }
                    state.copy(messages = updated, isStreaming = false)
                }
            }
        }
    }
}
