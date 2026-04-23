package br.com.cesarsicas.androidmovieflix.data.repository

import br.com.cesarsicas.androidmovieflix.data.local.ProfileCache
import br.com.cesarsicas.androidmovieflix.data.mapper.toUserModel
import br.com.cesarsicas.androidmovieflix.data.remote.api.UserApi
import br.com.cesarsicas.androidmovieflix.data.remote.dto.UpdateProfileRequestDto
import br.com.cesarsicas.androidmovieflix.domain.model.UserModel
import br.com.cesarsicas.androidmovieflix.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val profileCache: ProfileCache,
) : UserRepository {

    override suspend fun getProfile(): UserModel {
        profileCache.get()?.let { return it }
        return userApi.getProfile().toUserModel().also { profileCache.set(it) }
    }

    override suspend fun updateProfile(name: String, avatarUrl: String?): UserModel {
        return userApi.updateProfile(UpdateProfileRequestDto(name, avatarUrl))
            .toUserModel()
            .also { profileCache.set(it) }
    }
}
