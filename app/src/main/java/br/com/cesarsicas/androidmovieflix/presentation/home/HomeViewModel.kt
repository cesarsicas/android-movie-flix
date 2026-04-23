package br.com.cesarsicas.androidmovieflix.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetReleasesUseCase
import br.com.cesarsicas.androidmovieflix.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getReleasesUseCase: GetReleasesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<MovieModel>>>(UiState.Loading)
    val state: StateFlow<UiState<List<MovieModel>>> = _state

    init {
        loadReleases()
    }

    fun loadReleases() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = try {
                UiState.Success(getReleasesUseCase())
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Failed to load releases")
            }
        }
    }
}
