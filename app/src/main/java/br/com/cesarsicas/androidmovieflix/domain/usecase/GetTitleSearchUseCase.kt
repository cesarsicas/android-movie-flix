package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.data.mapper.toMovieModel
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.domain.repository.TitleRepository
import javax.inject.Inject

class GetTitleSearchUseCase @Inject constructor(
    private val titleRepository: TitleRepository,
) {
    suspend operator fun invoke(query: String): List<MovieModel> =
        titleRepository.search(query).map { it.toMovieModel() }
}
