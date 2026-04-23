package br.com.cesarsicas.androidmovieflix.data.repository

import br.com.cesarsicas.androidmovieflix.data.remote.api.TitleApi
import br.com.cesarsicas.androidmovieflix.data.remote.dto.MovieReleaseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleDetailsResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleSearchResponseDto
import br.com.cesarsicas.androidmovieflix.domain.repository.TitleRepository
import javax.inject.Inject

class TitleRepositoryImpl @Inject constructor(
    private val titleApi: TitleApi,
) : TitleRepository {

    override suspend fun getReleases(): List<MovieReleaseDto> = titleApi.getReleases()

    override suspend fun search(query: String): List<TitleSearchResponseDto> =
        titleApi.search(query)

    override suspend fun getDetails(externalId: Int): TitleDetailsResponseDto =
        titleApi.getDetails(externalId)
}
