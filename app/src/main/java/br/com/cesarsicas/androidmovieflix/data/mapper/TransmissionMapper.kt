package br.com.cesarsicas.androidmovieflix.data.mapper

import br.com.cesarsicas.androidmovieflix.data.remote.dto.TransmissionDto
import br.com.cesarsicas.androidmovieflix.domain.model.TransmissionModel

fun TransmissionDto.toTransmissionModel() = TransmissionModel(
    id = id,
    movieName = movieName,
    startTime = startTime,
    duration = duration,
    isActive = isActive,
)
