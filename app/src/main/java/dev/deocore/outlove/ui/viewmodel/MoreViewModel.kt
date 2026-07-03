package dev.deocore.outlove.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.deocore.outlove.data.model.*
import dev.deocore.outlove.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MoreUiState(
    val isLoading: Boolean = true,
    val daysTogether: Int = 0,
    val partner1Name: String = "Ты",
    val partner2Name: String = "Второй половинка",
    val startDate: String = "",
    val moods: List<MoodEntryDto> = emptyList(),
    val moodStats: MoodStatsDto = MoodStatsDto(),
    val importantDates: List<ImportantDateDto> = emptyList(),
    val error: String? = null,
)

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val moodsRepository: MoodsRepository,
    private val datesRepository: DatesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoreUiState())
    val uiState: StateFlow<MoreUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            authRepository.getRelationship().onSuccess { rel ->
                _uiState.update { it.copy(
                    daysTogether = rel.daysTogether,
                    partner1Name = rel.partner1Name,
                    partner2Name = rel.partner2Name,
                    startDate = rel.relationshipStartDate,
                )}
            }

            moodsRepository.getMoodStats().onSuccess { stats ->
                _uiState.update { it.copy(moodStats = stats) }
            }

            moodsRepository.getMoods().onSuccess { paginated ->
                _uiState.update { it.copy(moods = paginated.results) }
            }

            datesRepository.getImportantDates().onSuccess { dates ->
                _uiState.update { it.copy(importantDates = dates) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun saveSettings(partner1Name: String, partner2Name: String, startDate: String) {
        viewModelScope.launch {
            authRepository.getRelationship().onSuccess { rel ->
                // Update settings via API
                load()
            }
        }
    }

    fun addMood(mood: Int, date: String, note: String = "") {
        viewModelScope.launch {
            moodsRepository.createMood(mood, date, note).onSuccess {
                load()
            }
        }
    }
}
