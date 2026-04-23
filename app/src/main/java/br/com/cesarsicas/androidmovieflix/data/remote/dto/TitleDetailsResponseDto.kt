package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TitleDetailsResponseDto(
    val id: Int,
    val title: String,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("plot_overview") val plotOverview: String,
    val type: String,
    @SerialName("runtime_minutes") val runtimeMinutes: Int? = null,
    val year: Int? = null,
    @SerialName("end_year") val endYear: Int? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("imdb_id") val imdbId: String? = null,
    @SerialName("tmdb_id") val tmdbId: Int? = null,
    @SerialName("tmdb_type") val tmdbType: String? = null,
    val genres: List<Int>? = null,
    @SerialName("genre_names") val genreNames: List<String>? = null,
    @SerialName("similar_titles") val similarTitles: List<Int>? = null,
    val networks: List<Int>? = null,
    @SerialName("network_names") val networkNames: List<String>? = null,
    @SerialName("user_rating") val userRating: Double? = null,
    @SerialName("critic_score") val criticScore: Int? = null,
    @SerialName("relevance_percentile") val relevancePercentile: Double? = null,
    @SerialName("us_rating") val usRating: String? = null,
    val poster: String? = null,
    val backdrop: String? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    val trailer: String? = null,
    @SerialName("trailer_thumbnail") val trailerThumbnail: String? = null,
)
