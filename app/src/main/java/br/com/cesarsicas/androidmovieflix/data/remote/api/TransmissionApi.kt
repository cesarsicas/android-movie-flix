package br.com.cesarsicas.androidmovieflix.data.remote.api

import br.com.cesarsicas.androidmovieflix.data.remote.dto.StartTransmissionRequestDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TransmissionDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TransmissionMovieDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface TransmissionApi {
    @GET("transmissions/current")
    suspend fun getCurrentTransmission(): Response<TransmissionDto>

    @GET("transmissions/movies")
    suspend fun getAvailableMovies(): List<TransmissionMovieDto>

    @POST("transmissions/start")
    suspend fun startTransmission(@Body request: StartTransmissionRequestDto): TransmissionDto

    @PATCH("transmissions/stop")
    suspend fun stopTransmission(): Response<Unit>

    @Multipart
    @POST("transmissions/upload")
    suspend fun uploadMovie(
        @Part file: MultipartBody.Part,
        @Part("title") title: RequestBody,
    ): TransmissionMovieDto
}
