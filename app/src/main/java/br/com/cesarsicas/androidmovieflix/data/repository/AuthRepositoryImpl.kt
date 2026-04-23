package br.com.cesarsicas.androidmovieflix.data.repository

import br.com.cesarsicas.androidmovieflix.data.local.ProfileCache
import br.com.cesarsicas.androidmovieflix.data.local.TokenStore
import br.com.cesarsicas.androidmovieflix.data.mapper.toUserModel
import br.com.cesarsicas.androidmovieflix.data.remote.api.AuthApi
import br.com.cesarsicas.androidmovieflix.data.remote.dto.LoginRequestDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.SignupRequestDto
import br.com.cesarsicas.androidmovieflix.domain.model.UserModel
import br.com.cesarsicas.androidmovieflix.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore,
    private val profileCache: ProfileCache,
) : AuthRepository {

    override suspend fun login(email: String, password: String): UserModel {
        val response = authApi.login(LoginRequestDto(email, password))
        tokenStore.saveUserToken(response.token)
        return response.toUserModel().also { profileCache.set(it) }
    }

    override suspend fun signup(name: String, email: String, password: String): UserModel {
        val response = authApi.register(SignupRequestDto(name, email, password))
        tokenStore.saveUserToken(response.token)
        return response.toUserModel().also { profileCache.set(it) }
    }

    override suspend fun logout() {
        tokenStore.clearUserToken()
        profileCache.clear()
    }
}
