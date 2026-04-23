package br.com.cesarsicas.androidmovieflix.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetTitleSearchUseCase
import br.com.cesarsicas.androidmovieflix.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val getTitleSearchUseCase: GetTitleSearchUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<MovieModel>>>(UiState.Loading)
    val state: StateFlow<UiState<List<MovieModel>>> = _state

    fun search(query: String) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = try {
                UiState.Success(getTitleSearchUseCase(query))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Search failed")
            }
        }
    }
}
