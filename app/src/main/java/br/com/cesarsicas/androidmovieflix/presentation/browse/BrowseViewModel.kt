package br.com.cesarsicas.androidmovieflix.presentation.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.data.remote.dto.GenreDto
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetGenresUseCase
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetTitlesListUseCase
import br.com.cesarsicas.androidmovieflix.domain.usecase.TitlesListParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BrowseUiState(
    val titles: List<MovieModel> = emptyList(),
    val genres: List<GenreDto> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedType: String? = null,
    val selectedGenreIds: Set<Int> = emptySet(),
    val sortBy: String? = null,
    val ratingLow: Float? = null,
    val ratingHigh: Float? = null,
    val currentPage: Int = 1,
    val totalPages: Int? = null,
    val totalResults: Int = 0,
)

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val getTitlesListUseCase: GetTitlesListUseCase,
    private val getGenresUseCase: GetGenresUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(BrowseUiState())
    val state: StateFlow<BrowseUiState> = _state.asStateFlow()

    init {
        loadInitial()
    }

    private fun loadInitial() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val genresDeferred = async { getGenresUseCase() }
                val titlesDeferred = async { getTitlesListUseCase() }
                val genres = genresDeferred.await()
                val result = titlesDeferred.await()
                _state.update {
                    it.copy(
                        isLoading = false,
                        genres = genres,
                        titles = result.titles,
                        totalResults = result.totalResults,
                        totalPages = result.totalPages,
                        currentPage = 1,
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Failed to load") }
            }
        }
    }

    fun applyFilters(
        type: String? = _state.value.selectedType,
        genreIds: Set<Int> = _state.value.selectedGenreIds,
        sortBy: String? = _state.value.sortBy,
        ratingLow: Float? = _state.value.ratingLow,
        ratingHigh: Float? = _state.value.ratingHigh,
    ) {
        _state.update {
            it.copy(
                selectedType = type,
                selectedGenreIds = genreIds,
                sortBy = sortBy,
                ratingLow = ratingLow,
                ratingHigh = ratingHigh,
                currentPage = 1,
            )
        }
        loadTitles(page = 1)
    }

    fun loadNextPage() {
        val s = _state.value
        val nextPage = s.currentPage + 1
        if (s.totalPages != null && nextPage > s.totalPages) return
        _state.update { it.copy(currentPage = nextPage) }
        loadTitles(page = nextPage, append = true)
    }

    private fun loadTitles(page: Int, append: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val s = _state.value
                val params = TitlesListParams(
                    types = s.selectedType,
                    sortBy = s.sortBy,
                    genres = s.selectedGenreIds.takeIf { it.isNotEmpty() }?.joinToString(","),
                    ratingLow = s.ratingLow?.toString(),
                    ratingHigh = s.ratingHigh?.toString(),
                    page = page,
                )
                val result = getTitlesListUseCase(params)
                _state.update {
                    it.copy(
                        isLoading = false,
                        titles = if (append) it.titles + result.titles else result.titles,
                        totalResults = result.totalResults,
                        totalPages = result.totalPages,
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Failed to load") }
            }
        }
    }
}
