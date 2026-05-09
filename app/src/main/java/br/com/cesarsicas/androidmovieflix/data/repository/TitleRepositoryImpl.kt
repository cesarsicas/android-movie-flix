package br.com.cesarsicas.androidmovieflix.data.repository

import br.com.cesarsicas.androidmovieflix.BuildConfig
import br.com.cesarsicas.androidmovieflix.data.remote.api.TitleApi
import br.com.cesarsicas.androidmovieflix.data.remote.dto.AutocompleteSearchResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.GenreDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.MovieReleaseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.PersonResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.SaveTitleReviewRequestDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleDetailsResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleListResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleReviewDto
import br.com.cesarsicas.androidmovieflix.domain.repository.TitleRepository
import javax.inject.Inject

class TitleRepositoryImpl @Inject constructor(
    private val titleApi: TitleApi,
) : TitleRepository {

    override suspend fun getReleases(): List<MovieReleaseDto> = titleApi.getReleases()

    override suspend fun autocompleteSearch(query: String): AutocompleteSearchResponseDto =
        titleApi.autocompleteSearch(query)

    override suspend fun getDetails(externalId: Int): TitleDetailsResponseDto =
        titleApi.getDetails(externalId)

    override suspend fun getReviews(externalId: Int): List<TitleReviewDto> =
        titleApi.getReviews(externalId)

    override suspend fun saveReview(externalId: Int, rating: Int, review: String): TitleReviewDto =
        titleApi.saveReview(SaveTitleReviewRequestDto(externalId, rating, review))

    override fun getStreamUrl(externalId: Int): String =
        "${BuildConfig.BASE_URL}/titles/$externalId/stream"

    override suspend fun getTitlesList(
        types: String?,
        sortBy: String?,
        genres: String?,
        releaseDateStart: String?,
        releaseDateEnd: String?,
        ratingLow: String?,
        ratingHigh: String?,
        page: Int?,
        personId: Int?,
    ): TitleListResponseDto = titleApi.getTitlesList(
        types = types,
        sortBy = sortBy,
        genres = genres,
        releaseDateStart = releaseDateStart,
        releaseDateEnd = releaseDateEnd,
        ratingLow = ratingLow,
        ratingHigh = ratingHigh,
        page = page,
        personId = personId,
    )

    override suspend fun getGenres(): List<GenreDto> = titleApi.getGenres()

    override suspend fun getPerson(personId: Int): PersonResponseDto =
        titleApi.getPerson(personId)
}
