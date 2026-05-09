package br.com.cesarsicas.androidmovieflix.data.mapper

import br.com.cesarsicas.androidmovieflix.data.remote.dto.CastMemberDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.MovieReleaseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.PersonResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleDetailsResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleListItemDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleReviewDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleSearchResponseDto
import br.com.cesarsicas.androidmovieflix.domain.model.CastMemberModel
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.domain.model.PersonModel
import br.com.cesarsicas.androidmovieflix.domain.model.TitleDetailsModel
import br.com.cesarsicas.androidmovieflix.domain.model.TitleReviewModel

fun MovieReleaseDto.toMovieModel() = MovieModel(
    id = id,
    externalId = externalId,
    title = title,
    posterUrl = posterUrl.orEmpty(),
    description = "",
    releaseDate = sourceReleaseDate.orEmpty(),
    type = type,
)

fun TitleSearchResponseDto.toMovieModel() = MovieModel(
    id = id,
    externalId = id,
    title = name,
    posterUrl = imageUrl.orEmpty(),
    description = "",
    releaseDate = "",
    type = type.orEmpty(),
)

fun TitleListItemDto.toMovieModel() = MovieModel(
    id = id,
    externalId = if (externalId != 0) externalId else id,
    title = title,
    posterUrl = posterUrl.orEmpty(),
    description = "",
    releaseDate = year?.toString().orEmpty(),
    type = type,
)

fun TitleReviewDto.toTitleReviewModel() = TitleReviewModel(
    id = id,
    externalId = externalId,
    rating = rating,
    review = review,
    userName = userName,
    createdAt = createdAt,
)

fun CastMemberDto.toCastMemberModel() = CastMemberModel(
    personId = personId,
    name = name.orEmpty(),
    role = role,
    order = order,
    imageUrl = imageUrl,
)

fun TitleDetailsResponseDto.toTitleDetailsModel(): TitleDetailsModel {
    val allCast = cast?.map { it.toCastMemberModel() }
    return TitleDetailsModel(
        id = id,
        title = title,
        originalTitle = originalTitle,
        plotOverview = plotOverview,
        type = type,
        runtimeMinutes = runtimeMinutes,
        year = year,
        endYear = endYear,
        releaseDate = releaseDate,
        imdbId = imdbId,
        tmdbId = tmdbId,
        tmdbType = tmdbType,
        genres = genres,
        genreNames = genreNames,
        similarTitles = similarTitles,
        networks = networks,
        networkNames = networkNames,
        userRating = userRating,
        criticScore = criticScore,
        relevancePercentile = relevancePercentile,
        usRating = usRating,
        poster = poster,
        backdrop = backdrop,
        originalLanguage = originalLanguage,
        trailer = trailer,
        trailerThumbnail = trailerThumbnail,
        cast = allCast,
    )
}

fun PersonResponseDto.toPersonModel() = PersonModel(
    id = id,
    externalId = externalId,
    fullName = fullName,
    mainProfession = mainProfession,
    secondaryProfession = secondaryProfession,
    tertiaryProfession = tertiaryProfession,
    dateOfBirth = dateOfBirth,
    dateOfDeath = dateOfDeath,
    placeOfBirth = placeOfBirth,
    gender = gender,
    headshotUrl = headshotUrl,
    knownFor = knownFor,
)
