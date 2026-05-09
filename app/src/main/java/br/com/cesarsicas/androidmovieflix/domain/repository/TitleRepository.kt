package br.com.cesarsicas.androidmovieflix.domain.repository

import br.com.cesarsicas.androidmovieflix.data.remote.dto.AutocompleteSearchResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.GenreDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.MovieReleaseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.PersonResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleDetailsResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleListResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleReviewDto

interface TitleRepository {
    suspend fun getReleases(): List<MovieReleaseDto>
    suspend fun autocompleteSearch(query: String): AutocompleteSearchResponseDto
    suspend fun getDetails(externalId: Int): TitleDetailsResponseDto
    suspend fun getReviews(externalId: Int): List<TitleReviewDto>
    suspend fun saveReview(externalId: Int, rating: Int, review: String): TitleReviewDto
    fun getStreamUrl(externalId: Int): String
    suspend fun getTitlesList(
        types: String? = null,
        sortBy: String? = null,
        genres: String? = null,
        releaseDateStart: String? = null,
        releaseDateEnd: String? = null,
        ratingLow: String? = null,
        ratingHigh: String? = null,
        page: Int? = null,
        personId: Int? = null,
    ): TitleListResponseDto
    suspend fun getGenres(): List<GenreDto>
    suspend fun getPerson(personId: Int): PersonResponseDto
}
