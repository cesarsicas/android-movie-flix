package br.com.cesarsicas.androidmovieflix.data.repository

import br.com.cesarsicas.androidmovieflix.data.remote.api.UserApi
import br.com.cesarsicas.androidmovieflix.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @Suppress("unused") private val userApi: UserApi,
) : UserRepository
