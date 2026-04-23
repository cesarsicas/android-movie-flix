package br.com.cesarsicas.androidmovieflix.domain.repository

import br.com.cesarsicas.androidmovieflix.data.remote.dto.MovieReleaseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleDetailsResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleReviewDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleSearchResponseDto

interface TitleRepository {
    suspend fun getReleases(): List<MovieReleaseDto>
    suspend fun search(query: String): List<TitleSearchResponseDto>
    suspend fun getDetails(externalId: Int): TitleDetailsResponseDto
    suspend fun getReviews(externalId: Int): List<TitleReviewDto>
    suspend fun saveReview(externalId: Int, rating: Int, review: String): TitleReviewDto
    fun getStreamUrl(externalId: Int): String
}
