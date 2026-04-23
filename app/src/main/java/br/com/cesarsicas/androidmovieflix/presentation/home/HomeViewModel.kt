package br.com.cesarsicas.androidmovieflix.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.data.local.TokenStore
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetReleasesUseCase
import br.com.cesarsicas.androidmovieflix.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getReleasesUseCase: GetReleasesUseCase,
    tokenStore: TokenStore,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<MovieModel>>>(UiState.Loading)
    val state: StateFlow<UiState<List<MovieModel>>> = _state

    val isLoggedIn: StateFlow<Boolean> = tokenStore.userToken
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

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
