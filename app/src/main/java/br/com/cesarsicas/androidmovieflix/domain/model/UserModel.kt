package br.com.cesarsicas.androidmovieflix.domain.model

data class UserModel(
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val role: String,
)
