package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.domain.repository.TransmissionRepository
import javax.inject.Inject

class StopTransmissionUseCase @Inject constructor(
    private val transmissionRepository: TransmissionRepository,
) {
    suspend operator fun invoke() = transmissionRepository.stopTransmission()
}
