package br.com.cesarsicas.androidmovieflix.domain.model

data class TransmissionModel(
    val id: String,
    val movieName: String,
    val startTime: String,
    val duration: Int,
    val isActive: Boolean,
)
