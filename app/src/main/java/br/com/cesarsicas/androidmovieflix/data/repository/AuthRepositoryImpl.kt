package br.com.cesarsicas.androidmovieflix.data.repository

import br.com.cesarsicas.androidmovieflix.data.remote.api.AuthApi
import br.com.cesarsicas.androidmovieflix.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @Suppress("unused") private val authApi: AuthApi,
) : AuthRepository
