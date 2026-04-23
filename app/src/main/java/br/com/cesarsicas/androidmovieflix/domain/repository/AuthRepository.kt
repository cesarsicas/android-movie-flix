package br.com.cesarsicas.androidmovieflix.domain.repository

import br.com.cesarsicas.androidmovieflix.domain.model.UserModel

interface AuthRepository {
    suspend fun login(email: String, password: String): UserModel
    suspend fun signup(name: String, email: String, password: String): UserModel
    suspend fun logout()
}
