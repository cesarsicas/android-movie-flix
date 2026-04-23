package br.com.cesarsicas.androidmovieflix.data.remote

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink

class UriRequestBody(
    private val context: Context,
    private val uri: Uri,
    private val onProgress: (Int) -> Unit,
) : RequestBody() {

    override fun contentType(): MediaType? {
        val mimeType = context.contentResolver.getType(uri) ?: "video/mp4"
        return mimeType.toMediaTypeOrNull()
    }

    override fun contentLength(): Long {
        return context.contentResolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)
            ?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val idx = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (idx >= 0) cursor.getLong(idx) else -1L
                } else -1L
            } ?: -1L
    }

    override fun writeTo(sink: BufferedSink) {
        val length = contentLength()
        var written = 0L
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        context.contentResolver.openInputStream(uri)?.use { stream ->
            var read: Int
            while (stream.read(buffer).also { read = it } != -1) {
                sink.write(buffer, 0, read)
                written += read
                if (length > 0) onProgress((written * 100 / length).toInt())
            }
        } ?: error("Cannot open stream for $uri")
    }
}
