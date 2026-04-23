package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.domain.repository.TitleRepository
import javax.inject.Inject

class GetTitleStreamUseCase @Inject constructor(private val titleRepository: TitleRepository) {
    operator fun invoke(externalId: Int): String = titleRepository.getStreamUrl(externalId)
}
