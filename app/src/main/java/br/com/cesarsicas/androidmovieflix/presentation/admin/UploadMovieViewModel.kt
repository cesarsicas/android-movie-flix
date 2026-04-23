package br.com.cesarsicas.androidmovieflix.presentation.admin

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.cesarsicas.androidmovieflix.domain.usecase.UploadMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UploadMovieUiState(
    val selectedUri: Uri? = null,
    val selectedFileName: String? = null,
    val title: String = "",
    val isUploading: Boolean = false,
    val progress: Int = 0,
    val error: String? = null,
)

@HiltViewModel
class UploadMovieViewModel @Inject constructor(
    private val uploadMovieUseCase: UploadMovieUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(UploadMovieUiState())
    val state: StateFlow<UploadMovieUiState> = _state

    private val _uploadSuccess = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val uploadSuccess: SharedFlow<Unit> = _uploadSuccess

    fun onFileSelected(uri: Uri, fileName: String) {
        val title = fileName.substringBeforeLast(".")
        _state.value = _state.value.copy(
            selectedUri = uri,
            selectedFileName = fileName,
            title = title,
            error = null,
        )
    }

    fun onTitleChanged(title: String) {
        _state.value = _state.value.copy(title = title)
    }

    fun upload() {
        val uri = _state.value.selectedUri ?: run {
            _state.value = _state.value.copy(error = "Please select a file")
            return
        }
        val title = _state.value.title.trim()
        if (title.isBlank()) {
            _state.value = _state.value.copy(error = "Please enter a title")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isUploading = true, progress = 0, error = null)
            try {
                uploadMovieUseCase(uri, title) { progress ->
                    _state.value = _state.value.copy(progress = progress)
                }
                _state.value = _state.value.copy(progress = 100)
                delay(400)
                _uploadSuccess.emit(Unit)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isUploading = false,
                    progress = 0,
                    error = e.message ?: "Upload failed",
                )
            }
        }
    }
}
