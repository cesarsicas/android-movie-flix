package br.com.cesarsicas.androidmovieflix.data.local

import br.com.cesarsicas.androidmovieflix.domain.model.UserModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileCache @Inject constructor() {
    private var cache: UserModel? = null

    fun get(): UserModel? = cache
    fun set(user: UserModel) { cache = user }
    fun clear() { cache = null }
}
