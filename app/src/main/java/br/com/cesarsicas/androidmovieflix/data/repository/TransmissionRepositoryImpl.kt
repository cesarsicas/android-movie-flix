package br.com.cesarsicas.androidmovieflix.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import br.com.cesarsicas.androidmovieflix.BuildConfig
import br.com.cesarsicas.androidmovieflix.data.mapper.toTransmissionModel
import br.com.cesarsicas.androidmovieflix.data.mapper.toWatchPartyMovieModel
import br.com.cesarsicas.androidmovieflix.data.remote.UriRequestBody
import br.com.cesarsicas.androidmovieflix.data.remote.api.TransmissionApi
import br.com.cesarsicas.androidmovieflix.data.remote.dto.StartTransmissionRequestDto
import br.com.cesarsicas.androidmovieflix.domain.model.TransmissionModel
import br.com.cesarsicas.androidmovieflix.domain.model.WatchPartyMovieModel
import br.com.cesarsicas.androidmovieflix.domain.repository.TransmissionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class TransmissionRepositoryImpl @Inject constructor(
    private val transmissionApi: TransmissionApi,
    @ApplicationContext private val context: Context,
) : TransmissionRepository {

    override suspend fun getCurrentTransmission(): TransmissionModel? =
        transmissionApi.getCurrentTransmission().body()?.toTransmissionModel()

    override fun getLiveStreamUrl(): String = "${BuildConfig.BASE_URL}/live/stream.m3u8"

    override suspend fun getAvailableMovies(): List<WatchPartyMovieModel> =
        transmissionApi.getAvailableMovies().map { it.toWatchPartyMovieModel() }

    override suspend fun startTransmission(movieId: String): TransmissionModel =
        transmissionApi.startTransmission(StartTransmissionRequestDto(movieId)).toTransmissionModel()

    override suspend fun stopTransmission() {
        transmissionApi.stopTransmission()
    }

    override suspend fun uploadMovie(uri: Uri, title: String, onProgress: (Int) -> Unit): WatchPartyMovieModel {
        val fileName = getFileNameFromUri(uri) ?: "video.mp4"
        val fileBody = UriRequestBody(context, uri, onProgress)
        val filePart = MultipartBody.Part.createFormData("file", fileName, fileBody)
        val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
        return transmissionApi.uploadMovie(filePart, titlePart).toWatchPartyMovieModel()
    }

    private fun getFileNameFromUri(uri: Uri): String? =
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && idx >= 0) cursor.getString(idx) else null
        }
}
