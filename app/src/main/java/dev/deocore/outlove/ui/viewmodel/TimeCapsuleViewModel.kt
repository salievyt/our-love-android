package dev.deocore.outlove.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.deocore.outlove.data.model.*
import dev.deocore.outlove.data.repository.CapsulesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TimeCapsuleUiState(
    val isLoading: Boolean = true,
    val activeCapsules: List<TimeCapsuleDto> = emptyList(),
    val openedCapsules: List<TimeCapsuleDto> = emptyList(),
    val error: String? = null,
)

@HiltViewModel
class TimeCapsuleViewModel @Inject constructor(
    private val capsulesRepository: CapsulesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimeCapsuleUiState())
    val uiState: StateFlow<TimeCapsuleUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            capsulesRepository.getActiveCapsules().onSuccess { capsules ->
                _uiState.update { it.copy(activeCapsules = capsules) }
            }
            capsulesRepository.getOpenedCapsules().onSuccess { capsules ->
                _uiState.update { it.copy(openedCapsules = capsules) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun createCapsule(title: String, message: String, openDate: String) {
        viewModelScope.launch {
            capsulesRepository.createCapsule(title, message, openDate).onSuccess {
                load()
            }
        }
    }

    fun openCapsule(id: String) {
        viewModelScope.launch {
            capsulesRepository.openCapsule(id).onSuccess {
                load()
            }
        }
    }
}
