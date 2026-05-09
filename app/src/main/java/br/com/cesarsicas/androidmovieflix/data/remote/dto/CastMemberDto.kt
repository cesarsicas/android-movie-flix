package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CastMemberDto(
    @SerialName("person_id") val personId: Int,
    @SerialName("full_name") val name: String? = null,
    val role: String? = null,
    val order: Int? = null,
    @SerialName("headshot_url") val imageUrl: String? = null,
)
