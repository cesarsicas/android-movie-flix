package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.domain.repository.TransmissionRepository
import javax.inject.Inject

class GetLiveStreamUrlUseCase @Inject constructor(
    private val transmissionRepository: TransmissionRepository,
) {
    operator fun invoke(): String = transmissionRepository.getLiveStreamUrl()
}
