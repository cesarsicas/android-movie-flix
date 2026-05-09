package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.data.mapper.toMovieModel
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleSearchResponseDto
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.domain.repository.TitleRepository
import javax.inject.Inject

data class SearchResultItem(
    val id: Int,
    val name: String,
    val type: String?,
    val year: Int?,
    val resultType: String,
    val imageUrl: String?,
)

fun TitleSearchResponseDto.toSearchResultItem() = SearchResultItem(
    id = id,
    name = name,
    type = type,
    year = year,
    resultType = resultType,
    imageUrl = imageUrl,
)

class GetTitleSearchUseCase @Inject constructor(
    private val titleRepository: TitleRepository,
) {
    suspend operator fun invoke(query: String): List<SearchResultItem> =
        titleRepository.autocompleteSearch(query).results.map { it.toSearchResultItem() }
}
