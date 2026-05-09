package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonResponseDto(
    val id: Int,
    @SerialName("external_id") val externalId: Int = 0,
    @SerialName("full_name") val fullName: String,
    @SerialName("main_profession") val mainProfession: String? = null,
    @SerialName("secondary_profession") val secondaryProfession: String? = null,
    @SerialName("tertiary_profession") val tertiaryProfession: String? = null,
    @SerialName("date_of_birth") val dateOfBirth: String? = null,
    @SerialName("date_of_death") val dateOfDeath: String? = null,
    @SerialName("place_of_birth") val placeOfBirth: String? = null,
    val gender: String? = null,
    @SerialName("headshot_url") val headshotUrl: String? = null,
    @SerialName("known_for") val knownFor: List<Int>? = null,
)
