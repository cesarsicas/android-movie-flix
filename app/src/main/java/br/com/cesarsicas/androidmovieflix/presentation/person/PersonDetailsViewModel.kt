package br.com.cesarsicas.androidmovieflix.presentation.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.domain.model.PersonModel
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetPersonUseCase
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

data class PersonDetailsUiState(
    val person: PersonModel? = null,
    val filmography: List<MovieModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val getPersonUseCase: GetPersonUseCase,
    private val getTitlesListUseCase: GetTitlesListUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(PersonDetailsUiState())
    val state: StateFlow<PersonDetailsUiState> = _state.asStateFlow()

    fun load(personId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val personDeferred = async { getPersonUseCase(personId) }
                val filmographyDeferred = async {
                    getTitlesListUseCase(TitlesListParams(personId = personId)).titles
                }
                val person = personDeferred.await()
                val filmography = filmographyDeferred.await()
                _state.update {
                    it.copy(isLoading = false, person = person, filmography = filmography)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Failed to load") }
            }
        }
    }
}
