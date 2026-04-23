package br.com.cesarsicas.androidmovieflix.data.remote.api

import br.com.cesarsicas.androidmovieflix.data.remote.dto.TransmissionDto
import retrofit2.Response
import retrofit2.http.GET

interface TransmissionApi {
    @GET("transmissions/current")
    suspend fun getCurrentTransmission(): Response<TransmissionDto>
}
