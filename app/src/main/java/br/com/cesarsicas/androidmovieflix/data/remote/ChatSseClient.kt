package br.com.cesarsicas.androidmovieflix.data.remote

import br.com.cesarsicas.androidmovieflix.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@Serializable
private data class TokenContentDto(val content: String = "")

class ChatSseClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val json: Json,
) {
    fun sendMessage(message: String): Flow<String> = callbackFlow {
        val escapedMessage = message.replace("\\", "\\\\").replace("\"", "\\\"")
        val body = """{"message":"$escapedMessage"}""".toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("${BuildConfig.BASE_URL}/default/chat")
            .post(body)
            .build()

        val call = okHttpClient.newCall(request)
        try {
            val response = call.execute()
            val reader = response.body?.byteStream()?.bufferedReader()
                ?: throw Exception("Empty response body")

            var eventType = ""
            var dataLine = ""

            reader.forEachLine { line ->
                when {
                    line.startsWith("event:") -> eventType = line.removePrefix("event:").trim()
                    line.startsWith("data:") -> dataLine = line.removePrefix("data:").trim()
                    line.isEmpty() -> {
                        if (eventType == "token" && dataLine.isNotEmpty()) {
                            try {
                                val dto = json.decodeFromString<TokenContentDto>(dataLine)
                                if (dto.content.isNotEmpty()) trySend(dto.content)
                            } catch (_: Exception) {}
                        } else if (eventType == "done") {
                            close()
                        } else if (eventType == "error") {
                            close(Exception("Chat error"))
                        }
                        eventType = ""
                        dataLine = ""
                    }
                }
            }
            close()
        } catch (e: Exception) {
            close(e)
        }

        awaitClose { call.cancel() }
    }.flowOn(Dispatchers.IO)
}
