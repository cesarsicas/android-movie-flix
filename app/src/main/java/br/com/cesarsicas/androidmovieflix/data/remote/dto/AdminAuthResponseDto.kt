package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminAuthResponseDto(
    @SerialName("tokenJWT") val tokenJWT: String,
)
