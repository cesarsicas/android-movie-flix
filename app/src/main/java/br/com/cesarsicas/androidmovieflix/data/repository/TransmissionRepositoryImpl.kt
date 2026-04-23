package br.com.cesarsicas.androidmovieflix.data.repository

import br.com.cesarsicas.androidmovieflix.BuildConfig
import br.com.cesarsicas.androidmovieflix.data.mapper.toTransmissionModel
import br.com.cesarsicas.androidmovieflix.data.remote.api.TransmissionApi
import br.com.cesarsicas.androidmovieflix.domain.model.TransmissionModel
import br.com.cesarsicas.androidmovieflix.domain.repository.TransmissionRepository
import javax.inject.Inject

class TransmissionRepositoryImpl @Inject constructor(
    private val transmissionApi: TransmissionApi,
) : TransmissionRepository {

    override suspend fun getCurrentTransmission(): TransmissionModel? {
        val response = transmissionApi.getCurrentTransmission()
        return response.body()?.toTransmissionModel()
    }

    override fun getLiveStreamUrl(): String = "${BuildConfig.BASE_URL}/live/stream.m3u8"
}
