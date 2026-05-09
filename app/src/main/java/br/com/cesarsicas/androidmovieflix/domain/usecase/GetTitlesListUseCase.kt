package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.data.mapper.toMovieModel
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.domain.repository.TitleRepository
import javax.inject.Inject

data class TitlesListParams(
    val types: String? = null,
    val sortBy: String? = null,
    val genres: String? = null,
    val releaseDateStart: String? = null,
    val releaseDateEnd: String? = null,
    val ratingLow: String? = null,
    val ratingHigh: String? = null,
    val page: Int? = null,
    val personId: Int? = null,
)

data class TitleListResult(
    val titles: List<MovieModel>,
    val totalResults: Int,
    val totalPages: Int?,
    val page: Int?,
)

class GetTitlesListUseCase @Inject constructor(
    private val titleRepository: TitleRepository,
) {
    suspend operator fun invoke(params: TitlesListParams = TitlesListParams()): TitleListResult {
        val response = titleRepository.getTitlesList(
            types = params.types,
            sortBy = params.sortBy,
            genres = params.genres,
            releaseDateStart = params.releaseDateStart,
            releaseDateEnd = params.releaseDateEnd,
            ratingLow = params.ratingLow,
            ratingHigh = params.ratingHigh,
            page = params.page,
            personId = params.personId,
        )
        return TitleListResult(
            titles = response.titles.map { it.toMovieModel() },
            totalResults = response.totalResults,
            totalPages = response.totalPages,
            page = response.page,
        )
    }
}
