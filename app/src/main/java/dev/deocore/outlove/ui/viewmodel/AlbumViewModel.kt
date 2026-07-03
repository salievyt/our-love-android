package dev.deocore.outlove.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.deocore.outlove.data.model.*
import dev.deocore.outlove.data.repository.AlbumRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlbumUiState(
    val isLoading: Boolean = true,
    val photos: List<PhotoDto> = emptyList(),
    val collages: List<CollageDto> = emptyList(),
    val selectedYear: Int? = null,
    val error: String? = null,
)

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumUiState())
    val uiState: StateFlow<AlbumUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            albumRepository.getPhotos().onSuccess { paginated ->
                _uiState.update { it.copy(photos = paginated.results) }
            }

            albumRepository.getCollages().onSuccess { collages ->
                _uiState.update { it.copy(collages = collages) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun selectYear(year: Int?) {
        _uiState.update { it.copy(selectedYear = year) }
        viewModelScope.launch {
            albumRepository.getPhotos(yearTag = year).onSuccess { paginated ->
                _uiState.update { it.copy(photos = paginated.results) }
            }
        }
    }

    fun refresh() { load() }
}
