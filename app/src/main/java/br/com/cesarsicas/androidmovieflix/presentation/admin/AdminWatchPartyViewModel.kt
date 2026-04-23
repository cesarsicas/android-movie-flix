package br.com.cesarsicas.androidmovieflix.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.domain.model.TransmissionModel
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetCurrentTransmissionUseCase
import br.com.cesarsicas.androidmovieflix.domain.usecase.StopTransmissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

data class AdminWatchPartyUiState(
    val isLoading: Boolean = true,
    val transmission: TransmissionModel? = null,
    val error: String? = null,
    val isStopping: Boolean = false,
)

@HiltViewModel
class AdminWatchPartyViewModel @Inject constructor(
    private val getCurrentTransmissionUseCase: GetCurrentTransmissionUseCase,
    private val stopTransmissionUseCase: StopTransmissionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AdminWatchPartyUiState())
    val state: StateFlow<AdminWatchPartyUiState> = _state

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = AdminWatchPartyUiState(isLoading = true)
            try {
                _state.value = AdminWatchPartyUiState(
                    isLoading = false,
                    transmission = getCurrentTransmissionUseCase(),
                )
            } catch (e: Exception) {
                _state.value = AdminWatchPartyUiState(
                    isLoading = false,
                    error = e.message ?: "Failed to load transmission",
                )
            }
        }
    }

    fun stopTransmission() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isStopping = true)
            try {
                stopTransmissionUseCase()
                load()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isStopping = false,
                    error = e.message ?: "Failed to stop transmission",
                )
            }
        }
    }

    companion object {
        fun formatDuration(minutes: Int): String {
            val h = minutes / 60
            val m = minutes % 60
            return if (h > 0) "${h}h ${m}m" else "${m}m"
        }

        fun formatStartTime(startTime: String): String {
            return try {
                val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
                    .apply { timeZone = TimeZone.getTimeZone("UTC") }
                val output = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                val parsed = input.parse(startTime.take(19)) ?: return startTime
                output.format(parsed)
            } catch (e: Exception) {
                startTime
            }
        }
    }
}
