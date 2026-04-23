package br.com.cesarsicas.androidmovieflix.domain.usecase

import android.net.Uri
import br.com.cesarsicas.androidmovieflix.domain.model.WatchPartyMovieModel
import br.com.cesarsicas.androidmovieflix.domain.repository.TransmissionRepository
import javax.inject.Inject

class UploadMovieUseCase @Inject constructor(
    private val transmissionRepository: TransmissionRepository,
) {
    suspend operator fun invoke(
        uri: Uri,
        title: String,
        onProgress: (Int) -> Unit,
    ): WatchPartyMovieModel = transmissionRepository.uploadMovie(uri, title, onProgress)
}
