package br.com.cesarsicas.androidmovieflix.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.data.local.TokenStore
import br.com.cesarsicas.androidmovieflix.domain.usecase.AdminLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminLoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class AdminLoginViewModel @Inject constructor(
    private val adminLoginUseCase: AdminLoginUseCase,
    tokenStore: TokenStore,
) : ViewModel() {

    private val _state = MutableStateFlow(AdminLoginUiState())
    val state: StateFlow<AdminLoginUiState> = _state

    private val _loginSuccess = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val loginSuccess: SharedFlow<Unit> = _loginSuccess

    val isAdminLoggedIn: StateFlow<Boolean?> = tokenStore.adminToken
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AdminLoginUiState(isLoading = true)
            try {
                adminLoginUseCase(email, password)
                _state.value = AdminLoginUiState()
                _loginSuccess.emit(Unit)
            } catch (e: Exception) {
                _state.value = AdminLoginUiState(error = "Could not authenticate admin user")
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
