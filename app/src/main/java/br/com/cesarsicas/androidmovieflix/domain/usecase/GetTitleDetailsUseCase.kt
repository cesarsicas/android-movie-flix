package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.data.mapper.toTitleDetailsModel
import br.com.cesarsicas.androidmovieflix.domain.model.TitleDetailsModel
import br.com.cesarsicas.androidmovieflix.domain.repository.TitleRepository
import javax.inject.Inject

class GetTitleDetailsUseCase @Inject constructor(
    private val titleRepository: TitleRepository,
) {
    suspend operator fun invoke(externalId: Int): TitleDetailsModel =
        titleRepository.getDetails(externalId).toTitleDetailsModel()
}
