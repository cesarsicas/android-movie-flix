package br.com.cesarsicas.androidmovieflix.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequestDto(val name: String, val email: String, val password: String)
