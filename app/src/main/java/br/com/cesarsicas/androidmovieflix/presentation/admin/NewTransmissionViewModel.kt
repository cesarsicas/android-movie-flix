package br.com.cesarsicas.androidmovieflix.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.domain.model.WatchPartyMovieModel
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetAvailableMoviesUseCase
import br.com.cesarsicas.androidmovieflix.domain.usecase.StartTransmissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NewTransmissionUiState(
    val isLoading: Boolean = true,
    val movies: List<WatchPartyMovieModel> = emptyList(),
    val filteredMovies: List<WatchPartyMovieModel> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null,
    val isStarting: Boolean = false,
)

@HiltViewModel
class NewTransmissionViewModel @Inject constructor(
    private val getAvailableMoviesUseCase: GetAvailableMoviesUseCase,
    private val startTransmissionUseCase: StartTransmissionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(NewTransmissionUiState())
    val state: StateFlow<NewTransmissionUiState> = _state

    private val _startSuccess = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val startSuccess: SharedFlow<Unit> = _startSuccess

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            _state.value = NewTransmissionUiState(isLoading = true)
            try {
                val movies = getAvailableMoviesUseCase()
                _state.value = NewTransmissionUiState(isLoading = false, movies = movies, filteredMovies = movies)
            } catch (e: Exception) {
                _state.value = NewTransmissionUiState(
                    isLoading = false,
                    error = e.message ?: "Failed to load movies",
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        val filtered = if (query.isBlank()) {
            _state.value.movies
        } else {
            _state.value.movies.filter { it.title.contains(query, ignoreCase = true) }
        }
        _state.value = _state.value.copy(searchQuery = query, filteredMovies = filtered)
    }

    fun selectMovie(movieId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isStarting = true)
            try {
                startTransmissionUseCase(movieId)
                _startSuccess.emit(Unit)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isStarting = false,
                    error = e.message ?: "Failed to start transmission",
                )
            }
        }
    }
}
