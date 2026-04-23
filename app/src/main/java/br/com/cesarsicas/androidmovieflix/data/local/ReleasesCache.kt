package br.com.cesarsicas.androidmovieflix.data.local

import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReleasesCache @Inject constructor() {
    private var cache: List<MovieModel> = emptyList()

    fun isEmpty(): Boolean = cache.isEmpty()
    fun get(): List<MovieModel> = cache
    fun set(movies: List<MovieModel>) { cache = movies }
}
