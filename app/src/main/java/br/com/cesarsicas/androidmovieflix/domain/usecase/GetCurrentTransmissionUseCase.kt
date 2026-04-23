package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.domain.model.TransmissionModel
import br.com.cesarsicas.androidmovieflix.domain.repository.TransmissionRepository
import javax.inject.Inject

class GetCurrentTransmissionUseCase @Inject constructor(
    private val transmissionRepository: TransmissionRepository,
) {
    suspend operator fun invoke(): TransmissionModel? = transmissionRepository.getCurrentTransmission()
}
