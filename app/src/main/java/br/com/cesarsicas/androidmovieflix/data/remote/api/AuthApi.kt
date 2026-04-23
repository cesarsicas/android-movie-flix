package br.com.cesarsicas.androidmovieflix.data.remote.api

import br.com.cesarsicas.androidmovieflix.data.remote.dto.AdminAuthResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.AdminLoginRequestDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.AuthResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.LoginRequestDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.SignupRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: SignupRequestDto): AuthResponseDto

    @POST("auth/admin-login")
    suspend fun adminLogin(@Body request: AdminLoginRequestDto): AdminAuthResponseDto
}
