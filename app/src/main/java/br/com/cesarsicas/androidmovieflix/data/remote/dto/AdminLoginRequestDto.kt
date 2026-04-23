package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AdminLoginRequestDto(
    val login: String,
    val password: String,
)
