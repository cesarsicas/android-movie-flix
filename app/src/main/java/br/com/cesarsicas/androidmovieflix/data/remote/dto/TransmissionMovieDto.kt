package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TransmissionMovieDto(
    val id: String,
    val title: String,
    val duration: Int,
    val filename: String,
)
