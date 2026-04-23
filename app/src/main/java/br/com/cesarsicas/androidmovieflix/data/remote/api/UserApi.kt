package br.com.cesarsicas.androidmovieflix.data.remote.api

import br.com.cesarsicas.androidmovieflix.data.remote.dto.UpdateProfileRequestDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.UserProfileDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserApi {
    @GET("users/me")
    suspend fun getProfile(): UserProfileDto

    @PUT("users/me")
    suspend fun updateProfile(@Body request: UpdateProfileRequestDto): UserProfileDto
}
