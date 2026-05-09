package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TitleSearchResponseDto(
    val name: String,
    val relevance: Double? = null,
    val type: String? = null,
    val id: Int,
    val year: Int? = null,
    @SerialName("result_type") val resultType: String = "title",
    @SerialName("tmdb_id") val tmdbId: Int? = null,
    @SerialName("tmdb_type") val tmdbType: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
)

@Serializable
data class AutocompleteSearchResponseDto(
    val results: List<TitleSearchResponseDto>,
)
