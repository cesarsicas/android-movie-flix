package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TitleReviewDto(
    val id: Int,
    @SerialName("external_id") val externalId: Int,
    val rating: Int? = null,
    val review: String? = null,
    @SerialName("user_name") val userName: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
)
