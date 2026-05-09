package br.com.cesarsicas.androidmovieflix.domain.model

data class PersonModel(
    val id: Int,
    val externalId: Int,
    val fullName: String,
    val mainProfession: String?,
    val secondaryProfession: String?,
    val tertiaryProfession: String?,
    val dateOfBirth: String?,
    val dateOfDeath: String?,
    val placeOfBirth: String?,
    val gender: String?,
    val headshotUrl: String?,
    val knownFor: List<Int>?,
)
