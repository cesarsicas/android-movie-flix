package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TitleSearchResponseDto(
    val name: String,
    val relevance: Int,
    val type: String,
    val id: Int,
    val year: Int,
    @SerialName("result_type") val resultType: String,
    @SerialName("tmdb_id") val tmdbId: Int,
    @SerialName("tmdb_type") val tmdbType: String,
    @SerialName("image_url") val imageUrl: String,
)
