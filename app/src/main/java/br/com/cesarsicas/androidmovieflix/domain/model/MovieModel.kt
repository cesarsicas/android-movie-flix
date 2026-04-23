package br.com.cesarsicas.androidmovieflix.domain.model

data class MovieModel(
    val id: Int,
    val externalId: Int,
    val title: String,
    val posterUrl: String,
    val description: String,
    val releaseDate: String,
    val type: String,
)
