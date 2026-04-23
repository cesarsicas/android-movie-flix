package br.com.cesarsicas.androidmovieflix.data.repository

import br.com.cesarsicas.androidmovieflix.data.remote.api.TitleApi
import br.com.cesarsicas.androidmovieflix.domain.repository.TitleRepository
import javax.inject.Inject

class TitleRepositoryImpl @Inject constructor(
    @Suppress("unused") private val titleApi: TitleApi,
) : TitleRepository
