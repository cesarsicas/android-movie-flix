package br.com.cesarsicas.androidmovieflix.domain.model

data class TitleReviewModel(
    val id: Int,
    val externalId: Int,
    val rating: Int?,
    val review: String?,
    val userName: String?,
    val createdAt: String?,
)
