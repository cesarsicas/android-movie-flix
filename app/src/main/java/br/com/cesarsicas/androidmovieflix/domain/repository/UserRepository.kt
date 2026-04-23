package br.com.cesarsicas.androidmovieflix.domain.repository

import br.com.cesarsicas.androidmovieflix.domain.model.UserModel

interface UserRepository {
    suspend fun getProfile(): UserModel
    suspend fun updateProfile(name: String, avatarUrl: String?): UserModel
}
