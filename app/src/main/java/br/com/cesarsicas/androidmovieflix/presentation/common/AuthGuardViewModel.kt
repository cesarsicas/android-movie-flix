package br.com.cesarsicas.androidmovieflix.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.data.local.TokenStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuthGuardViewModel @Inject constructor(tokenStore: TokenStore) : ViewModel() {

    val isUserLoggedIn: StateFlow<Boolean?> = tokenStore.userToken
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val isAdminLoggedIn: StateFlow<Boolean?> = tokenStore.adminToken
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
