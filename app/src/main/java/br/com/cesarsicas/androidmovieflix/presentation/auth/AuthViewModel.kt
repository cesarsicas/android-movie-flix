package br.com.cesarsicas.androidmovieflix.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.domain.usecase.LoginUseCase
import br.com.cesarsicas.androidmovieflix.domain.usecase.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    private val _loginSuccess = MutableSharedFlow<Unit>()
    val loginSuccess: SharedFlow<Unit> = _loginSuccess

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(error = "Email and password are required")
            return
        }
        viewModelScope.launch {
            _state.value = AuthUiState(isLoading = true)
            try {
                loginUseCase(email.trim(), password)
                _loginSuccess.emit(Unit)
            } catch (e: Exception) {
                _state.value = AuthUiState(error = e.message ?: "Login failed")
            }
        }
    }

    fun signup(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(error = "All fields are required")
            return
        }
        viewModelScope.launch {
            _state.value = AuthUiState(isLoading = true)
            try {
                signupUseCase(name.trim(), email.trim(), password)
                _loginSuccess.emit(Unit)
            } catch (e: Exception) {
                _state.value = AuthUiState(error = e.message ?: "Signup failed")
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}
