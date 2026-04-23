package br.com.cesarsicas.androidmovieflix.data.mapper

import br.com.cesarsicas.androidmovieflix.data.remote.dto.TransmissionDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TransmissionMovieDto
import br.com.cesarsicas.androidmovieflix.domain.model.TransmissionModel
import br.com.cesarsicas.androidmovieflix.domain.model.WatchPartyMovieModel

fun TransmissionDto.toTransmissionModel() = TransmissionModel(
    id = id,
    movieName = movieName,
    startTime = startTime,
    duration = duration,
    isActive = isActive,
)

fun TransmissionMovieDto.toWatchPartyMovieModel() = WatchPartyMovieModel(
    id = id,
    title = title,
    duration = duration,
    filename = filename,
)
