package br.com.cesarsicas.androidmovieflix.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.data.local.TokenStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminScaffoldViewModel @Inject constructor(
    private val tokenStore: TokenStore,
) : ViewModel() {

    private val _logoutEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val logoutEvent: SharedFlow<Unit> = _logoutEvent

    fun logout() {
        viewModelScope.launch {
            tokenStore.clearAdminToken()
            _logoutEvent.emit(Unit)
        }
    }
}
