package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.domain.model.WatchPartyMovieModel
import br.com.cesarsicas.androidmovieflix.domain.repository.TransmissionRepository
import javax.inject.Inject

class GetAvailableMoviesUseCase @Inject constructor(
    private val transmissionRepository: TransmissionRepository,
) {
    suspend operator fun invoke(): List<WatchPartyMovieModel> =
        transmissionRepository.getAvailableMovies()
}
