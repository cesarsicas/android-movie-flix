package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.data.mapper.toPersonModel
import br.com.cesarsicas.androidmovieflix.domain.model.PersonModel
import br.com.cesarsicas.androidmovieflix.domain.repository.TitleRepository
import javax.inject.Inject

class GetPersonUseCase @Inject constructor(
    private val titleRepository: TitleRepository,
) {
    suspend operator fun invoke(personId: Int): PersonModel =
        titleRepository.getPerson(personId).toPersonModel()
}
