package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TitleListResponseDto(
    val titles: List<TitleListItemDto>,
    val page: Int? = null,
    @SerialName("total_results") val totalResults: Int = 0,
    @SerialName("total_pages") val totalPages: Int? = null,
)

@Serializable
data class TitleListItemDto(
    val id: Int,
    @SerialName("external_id") val externalId: Int = 0,
    val title: String,
    val type: String,
    val year: Int? = null,
    @SerialName("poster_url") val posterUrl: String? = null,
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String,
)
