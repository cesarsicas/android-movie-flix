package br.com.cesarsicas.androidmovieflix.domain.repository

import br.com.cesarsicas.androidmovieflix.domain.model.TransmissionModel

interface TransmissionRepository {
    suspend fun getCurrentTransmission(): TransmissionModel?
    fun getLiveStreamUrl(): String
}
