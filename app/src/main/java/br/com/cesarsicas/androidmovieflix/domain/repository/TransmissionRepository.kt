package br.com.cesarsicas.androidmovieflix.domain.repository

import android.net.Uri
import br.com.cesarsicas.androidmovieflix.domain.model.TransmissionModel
import br.com.cesarsicas.androidmovieflix.domain.model.WatchPartyMovieModel

interface TransmissionRepository {
    suspend fun getCurrentTransmission(): TransmissionModel?
    fun getLiveStreamUrl(): String
    suspend fun getAvailableMovies(): List<WatchPartyMovieModel>
    suspend fun startTransmission(movieId: String): TransmissionModel
    suspend fun stopTransmission()
    suspend fun uploadMovie(uri: Uri, title: String, onProgress: (Int) -> Unit): WatchPartyMovieModel
}
