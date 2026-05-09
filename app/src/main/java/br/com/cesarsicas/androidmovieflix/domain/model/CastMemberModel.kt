package br.com.cesarsicas.androidmovieflix.domain.model

data class CastMemberModel(
    val personId: Int,
    val name: String,
    val role: String?,
    val order: Int?,
    val imageUrl: String?,
)
