package br.com.cesarsicas.androidmovieflix.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetTitleSearchUseCase
import br.com.cesarsicas.androidmovieflix.domain.usecase.SearchResultItem
import br.com.cesarsicas.androidmovieflix.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SearchFilter { ALL, MOVIES, TV, PEOPLE }

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val getTitleSearchUseCase: GetTitleSearchUseCase,
) : ViewModel() {

    private val _allResults = MutableStateFlow<UiState<List<SearchResultItem>>>(UiState.Loading)
    val allResults: StateFlow<UiState<List<SearchResultItem>>> = _allResults

    private val _filter = MutableStateFlow(SearchFilter.ALL)
    val filter: StateFlow<SearchFilter> = _filter

    val state: StateFlow<UiState<List<SearchResultItem>>> = combine(_allResults, _filter) { raw, filter ->
        when (raw) {
            is UiState.Success -> UiState.Success(raw.data.filter { item ->
                when (filter) {
                    SearchFilter.ALL -> true
                    SearchFilter.MOVIES -> item.type == "movie"
                    SearchFilter.TV -> item.type in setOf("tv_series", "tv_miniseries", "tv_special")
                    SearchFilter.PEOPLE -> item.resultType == "person"
                }
            })
            else -> raw
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    fun search(query: String) {
        viewModelScope.launch {
            _allResults.value = UiState.Loading
            _allResults.value = try {
                UiState.Success(getTitleSearchUseCase(query))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Search failed")
            }
        }
    }

    fun setFilter(filter: SearchFilter) {
        _filter.value = filter
    }
}
