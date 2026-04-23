package br.com.cesarsicas.androidmovieflix.data.mapper

import br.com.cesarsicas.androidmovieflix.data.remote.dto.MovieReleaseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleDetailsResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleSearchResponseDto
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.domain.model.TitleDetailsModel

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
    posterUrl = imageUrl,
    description = "",
    releaseDate = "",
    type = type,
)

fun TitleDetailsResponseDto.toTitleDetailsModel() = TitleDetailsModel(
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
)
