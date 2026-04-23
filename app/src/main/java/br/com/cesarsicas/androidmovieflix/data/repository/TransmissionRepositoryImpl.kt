package br.com.cesarsicas.androidmovieflix.data.repository

import br.com.cesarsicas.androidmovieflix.data.remote.api.TransmissionApi
import br.com.cesarsicas.androidmovieflix.domain.repository.TransmissionRepository
import javax.inject.Inject

class TransmissionRepositoryImpl @Inject constructor(
    @Suppress("unused") private val transmissionApi: TransmissionApi,
) : TransmissionRepository
