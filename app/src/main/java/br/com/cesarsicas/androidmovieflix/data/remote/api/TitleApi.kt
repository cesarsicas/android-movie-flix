package br.com.cesarsicas.androidmovieflix.data.remote.api

import br.com.cesarsicas.androidmovieflix.data.remote.dto.AutocompleteSearchResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.GenreDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.MovieReleaseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.PersonResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.SaveTitleReviewRequestDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleDetailsResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleListResponseDto
import br.com.cesarsicas.androidmovieflix.data.remote.dto.TitleReviewDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TitleApi {

    @GET("titles/releases")
    suspend fun getReleases(
        @Query("useCache") useCache: Boolean = true,
    ): List<MovieReleaseDto>

    @GET("titles/autocomplete-search")
    suspend fun autocompleteSearch(
        @Query("query") query: String,
    ): AutocompleteSearchResponseDto

    @GET("titles/{externalId}")
    suspend fun getDetails(
        @Path("externalId") externalId: Int,
        @Query("useCache") useCache: Boolean = true,
    ): TitleDetailsResponseDto

    @GET("titles/{externalId}/reviews")
    suspend fun getReviews(
        @Path("externalId") externalId: Int,
    ): List<TitleReviewDto>

    @POST("reviews")
    suspend fun saveReview(
        @Body request: SaveTitleReviewRequestDto,
    ): TitleReviewDto

    @GET("titles/list")
    suspend fun getTitlesList(
        @Query("types") types: String? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("genres") genres: String? = null,
        @Query("release_date_start") releaseDateStart: String? = null,
        @Query("release_date_end") releaseDateEnd: String? = null,
        @Query("user_rating_low") ratingLow: String? = null,
        @Query("user_rating_high") ratingHigh: String? = null,
        @Query("page") page: Int? = null,
        @Query("person_id") personId: Int? = null,
        @Query("useCache") useCache: Boolean = false,
    ): TitleListResponseDto

    @GET("titles/genres")
    suspend fun getGenres(
        @Query("useCache") useCache: Boolean = true,
    ): List<GenreDto>

    @GET("titles/person/{personId}")
    suspend fun getPerson(
        @Path("personId") personId: Int,
        @Query("useCache") useCache: Boolean = true,
    ): PersonResponseDto
}
