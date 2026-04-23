package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveTitleReviewRequestDto(
    @SerialName("external_id") val externalId: Int,
    val rating: Int,
    val review: String,
)
