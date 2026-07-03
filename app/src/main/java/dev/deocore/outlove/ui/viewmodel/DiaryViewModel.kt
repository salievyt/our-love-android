package dev.deocore.outlove.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.deocore.outlove.data.model.*
import dev.deocore.outlove.data.repository.DiaryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiaryUiState(
    val isLoading: Boolean = true,
    val entries: List<DiaryEntryDto> = emptyList(),
    val todayEntry: DiaryEntryDto? = null,
    val error: String? = null,
)

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            diaryRepository.getTodayEntry().onSuccess { entry ->
                _uiState.update { it.copy(todayEntry = entry) }
            }

            diaryRepository.getEntries().onSuccess { paginated ->
                _uiState.update { it.copy(entries = paginated.results) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun createEntry(date: String, note: String, mood: Int? = null) {
        viewModelScope.launch {
            diaryRepository.createEntry(date, note, mood).onSuccess {
                load()
            }
        }
    }
}
