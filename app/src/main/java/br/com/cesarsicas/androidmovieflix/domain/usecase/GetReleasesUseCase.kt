package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.data.local.ReleasesCache
import br.com.cesarsicas.androidmovieflix.data.mapper.toMovieModel
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.domain.repository.TitleRepository
import javax.inject.Inject

class GetReleasesUseCase @Inject constructor(
    private val titleRepository: TitleRepository,
    private val releasesCache: ReleasesCache,
) {
    suspend operator fun invoke(): List<MovieModel> {
        if (!releasesCache.isEmpty()) return releasesCache.get()
        val movies = titleRepository.getReleases().map { it.toMovieModel() }
        releasesCache.set(movies)
        return movies
    }
}
