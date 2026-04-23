package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieReleaseDto(
    val id: Int,
    @SerialName("external_id") val externalId: Int,
    val title: String,
    val type: String,
    @SerialName("imdb_id") val imdbId: String? = null,
    @SerialName("tmdb_id") val tmdbId: Int? = null,
    @SerialName("tmdb_type") val tmdbType: String? = null,
    @SerialName("season_number") val seasonNumber: Int? = null,
    @SerialName("poster_url") val posterUrl: String? = null,
    @SerialName("source_release_date") val sourceReleaseDate: String? = null,
    @SerialName("source_id") val sourceId: Int? = null,
    @SerialName("source_name") val sourceName: String? = null,
    @SerialName("is_original") val isOriginal: Int? = null,
)
