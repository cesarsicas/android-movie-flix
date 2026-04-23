package br.com.cesarsicas.androidmovieflix.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.domain.model.UserModel
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetProfileUseCase
import br.com.cesarsicas.androidmovieflix.domain.usecase.UpdateProfileUseCase
import br.com.cesarsicas.androidmovieflix.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<UserModel>>(UiState.Loading)
    val state: StateFlow<UiState<UserModel>> = _state

    private val _saveSuccess = MutableSharedFlow<Unit>()
    val saveSuccess: SharedFlow<Unit> = _saveSuccess

    init {
        viewModelScope.launch {
            _state.value = try {
                UiState.Success(getProfileUseCase())
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    fun save(name: String, avatarUrl: String?) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = try {
                val updated = updateProfileUseCase(name.trim(), avatarUrl?.ifBlank { null })
                _saveSuccess.emit(Unit)
                UiState.Success(updated)
            } catch (e: Exception) {
                UiState.Error(e.message ?: "Failed to update profile")
            }
        }
    }
}
