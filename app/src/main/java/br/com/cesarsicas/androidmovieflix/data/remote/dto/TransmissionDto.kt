package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TransmissionDto(
    val id: String,
    val movieName: String,
    val startTime: String,
    val duration: Int,
    val isActive: Boolean,
)
