package br.com.cesarsicas.androidmovieflix.domain.usecase

import br.com.cesarsicas.androidmovieflix.data.mapper.toTitleReviewModel
import br.com.cesarsicas.androidmovieflix.domain.model.TitleReviewModel
import br.com.cesarsicas.androidmovieflix.domain.repository.TitleRepository
import javax.inject.Inject

class GetTitleReviewsUseCase @Inject constructor(private val titleRepository: TitleRepository) {
    suspend operator fun invoke(externalId: Int): List<TitleReviewModel> =
        titleRepository.getReviews(externalId).map { it.toTitleReviewModel() }
}
