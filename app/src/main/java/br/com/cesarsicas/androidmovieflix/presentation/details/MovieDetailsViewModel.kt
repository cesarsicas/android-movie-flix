package br.com.cesarsicas.androidmovieflix.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.data.local.TokenStore
import br.com.cesarsicas.androidmovieflix.domain.model.TitleDetailsModel
import br.com.cesarsicas.androidmovieflix.domain.model.TitleReviewModel
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetTitleDetailsUseCase
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetTitleReviewsUseCase
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetTitleStreamUseCase
import br.com.cesarsicas.androidmovieflix.domain.usecase.SaveTitleReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieDetailsUiState(
    val details: TitleDetailsModel? = null,
    val reviews: List<TitleReviewModel> = emptyList(),
    val streamUrl: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val showReviewDialog: Boolean = false,
    val isSavingReview: Boolean = false,
    val reviewError: String? = null,
)

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getTitleDetailsUseCase: GetTitleDetailsUseCase,
    private val getTitleReviewsUseCase: GetTitleReviewsUseCase,
    private val getTitleStreamUseCase: GetTitleStreamUseCase,
    private val saveTitleReviewUseCase: SaveTitleReviewUseCase,
    tokenStore: TokenStore,
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailsUiState())
    val state: StateFlow<MovieDetailsUiState> = _state

    val isLoggedIn: StateFlow<Boolean> = tokenStore.userToken
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private var currentExternalId: Int? = null

    fun loadDetails(externalId: Int) {
        currentExternalId = externalId
        viewModelScope.launch {
            _state.value = MovieDetailsUiState(isLoading = true)
            try {
                val detailsDeferred = async { getTitleDetailsUseCase(externalId) }
                val reviewsDeferred = async { getTitleReviewsUseCase(externalId) }
                val streamUrl = getTitleStreamUseCase(externalId)
                _state.value = MovieDetailsUiState(
                    details = detailsDeferred.await(),
                    reviews = reviewsDeferred.await(),
                    streamUrl = streamUrl,
                    isLoading = false,
                )
            } catch (e: Exception) {
                _state.value = MovieDetailsUiState(
                    isLoading = false,
                    error = e.message ?: "Failed to load details",
                )
            }
        }
    }

    fun openReviewDialog() {
        _state.value = _state.value.copy(showReviewDialog = true, reviewError = null)
    }

    fun closeReviewDialog() {
        _state.value = _state.value.copy(showReviewDialog = false, reviewError = null)
    }

    fun submitReview(rating: Int, review: String) {
        val externalId = currentExternalId ?: return
        viewModelScope.launch {
            _state.value = _state.value.copy(isSavingReview = true, reviewError = null)
            try {
                val saved = saveTitleReviewUseCase(externalId, rating, review)
                _state.value = _state.value.copy(
                    reviews = _state.value.reviews + saved,
                    isSavingReview = false,
                    showReviewDialog = false,
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSavingReview = false,
                    reviewError = e.message ?: "Failed to submit review",
                )
            }
        }
    }
}
