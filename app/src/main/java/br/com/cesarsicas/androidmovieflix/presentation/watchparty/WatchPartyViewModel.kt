package br.com.cesarsicas.androidmovieflix.presentation.watchparty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.domain.model.TransmissionModel
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetCurrentTransmissionUseCase
import br.com.cesarsicas.androidmovieflix.domain.usecase.GetLiveStreamUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

data class WatchPartyUiState(
    val isLoading: Boolean = true,
    val transmission: TransmissionModel? = null,
    val elapsedSeconds: Long = 0L,
)

@HiltViewModel
class WatchPartyViewModel @Inject constructor(
    private val getCurrentTransmissionUseCase: GetCurrentTransmissionUseCase,
    getLiveStreamUrlUseCase: GetLiveStreamUrlUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(WatchPartyUiState())
    val state: StateFlow<WatchPartyUiState> = _state

    val streamUrl: String = getLiveStreamUrlUseCase()

    private var pollingJob: Job? = null
    private var elapsedJob: Job? = null

    init {
        loadTransmission()
    }

    private fun loadTransmission() {
        viewModelScope.launch {
            _state.value = WatchPartyUiState(isLoading = true)
            val transmission = runCatching { getCurrentTransmissionUseCase() }.getOrNull()
            if (transmission != null) {
                setTransmission(transmission)
            } else {
                _state.value = WatchPartyUiState(isLoading = false)
                startPolling()
            }
        }
    }

    private fun startPolling() {
        pollingJob = viewModelScope.launch {
            while (isActive) {
                delay(5_000)
                val result = runCatching { getCurrentTransmissionUseCase() }.getOrNull()
                if (result != null) {
                    setTransmission(result)
                    break
                }
            }
        }
    }

    private fun setTransmission(transmission: TransmissionModel) {
        pollingJob?.cancel()
        _state.value = _state.value.copy(isLoading = false, transmission = transmission)
        startElapsedTimer(transmission.startTime)
    }

    private fun startElapsedTimer(startTime: String) {
        val startMillis = parseIsoTimestamp(startTime)
        elapsedJob = viewModelScope.launch {
            while (isActive) {
                val elapsed = (System.currentTimeMillis() - startMillis) / 1_000L
                _state.value = _state.value.copy(elapsedSeconds = maxOf(0L, elapsed))
                delay(1_000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        pollingJob?.cancel()
        elapsedJob?.cancel()
    }

    companion object {
        private fun parseIsoTimestamp(timestamp: String): Long = runCatching {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
                .apply { timeZone = TimeZone.getTimeZone("UTC") }
                .parse(timestamp.take(19))?.time ?: System.currentTimeMillis()
        }.getOrElse { System.currentTimeMillis() }

        fun formatElapsed(seconds: Long): String {
            val h = seconds / 3600
            val m = (seconds % 3600) / 60
            val s = seconds % 60
            return when {
                h > 0 -> "${h}h ${m}m ${s}s"
                m > 0 -> "${m}m ${s}s"
                else -> "${s}s"
            }
        }
    }
}
