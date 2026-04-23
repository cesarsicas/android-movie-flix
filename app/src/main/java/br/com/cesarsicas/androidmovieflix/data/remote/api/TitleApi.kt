package br.com.cesarsicas.androidmovieflix.data.remote.api

import br.com.cesarsicas.androidmovieflix.data.remote.dto.MovieReleaseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleDetailsResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TitleApi {

    @GET("titles/releases")
    suspend fun getReleases(
        @Query("useCache") useCache: Boolean = true,
    ): List<MovieReleaseDto>

    @GET("titles/search")
    suspend fun search(
        @Query("query") query: String,
        @Query("useCache") useCache: Boolean = true,
    ): List<TitleSearchResponseDto>

    @GET("titles/{externalId}")
    suspend fun getDetails(
        @Path("externalId") externalId: Int,
        @Query("useCache") useCache: Boolean = true,
    ): TitleDetailsResponseDto
}
