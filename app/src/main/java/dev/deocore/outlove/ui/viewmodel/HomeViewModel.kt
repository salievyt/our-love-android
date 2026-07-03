package dev.deocore.outlove.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.deocore.outlove.data.model.*
import dev.deocore.outlove.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val daysTogether: Int = 0,
    val startDate: String = "",
    val partner1Name: String = "Ты",
    val partner2Name: String = "Второй половинка",
    val upcomingDates: List<ImportantDateDto> = emptyList(),
    val todayTask: DailyTaskDto? = null,
    val todayQuestion: QuestionDto? = null,
    val todayMood: MoodEntryDto? = null,
    val error: String? = null,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tasksRepository: TasksRepository,
    private val questionsRepository: QuestionsRepository,
    private val moodsRepository: MoodsRepository,
    private val datesRepository: DatesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init { loadAll() }

    fun loadAll() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Load relationship
            authRepository.getRelationship().onSuccess { rel ->
                _uiState.update { it.copy(
                    daysTogether = rel.daysTogether,
                    startDate = rel.relationshipStartDate,
                    partner1Name = rel.partner1Name,
                    partner2Name = rel.partner2Name,
                )}
            }

            // Load upcoming dates
            datesRepository.getUpcomingDates().onSuccess { dates ->
                _uiState.update { it.copy(upcomingDates = dates) }
            }

            // Load today's task
            tasksRepository.getTodayTask().onSuccess { task ->
                _uiState.update { it.copy(todayTask = task) }
            }

            // Load today's question
            questionsRepository.getTodayQuestion().onSuccess { q ->
                _uiState.update { it.copy(todayQuestion = q) }
            }

            // Load today's mood
            moodsRepository.getTodayMood().onSuccess { mood ->
                _uiState.update { it.copy(todayMood = mood) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun refresh() { loadAll() }
}
