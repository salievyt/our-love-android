package dev.deocore.outlove.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.deocore.outlove.data.model.*
import dev.deocore.outlove.data.repository.PlacesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val isLoading: Boolean = true,
    val places: List<PlaceDto> = emptyList(),
    val error: String? = null,
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            placesRepository.getPlaces().onSuccess { places ->
                _uiState.update { it.copy(places = places, isLoading = false) }
            }.onFailure { e ->
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun createPlace(title: String, description: String, emoji: String, lat: Double, lng: Double) {
        viewModelScope.launch {
            placesRepository.createPlace(title, description, emoji, lat, lng).onSuccess {
                load()
            }
        }
    }
}
