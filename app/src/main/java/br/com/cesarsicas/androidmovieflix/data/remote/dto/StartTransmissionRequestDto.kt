package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StartTransmissionRequestDto(
    val movieId: String,
)
