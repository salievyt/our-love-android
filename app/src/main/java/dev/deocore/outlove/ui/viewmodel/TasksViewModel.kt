package dev.deocore.outlove.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.deocore.outlove.data.model.*
import dev.deocore.outlove.data.repository.TasksRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TasksUiState(
    val isLoading: Boolean = true,
    val todayTask: DailyTaskDto? = null,
    val taskHistory: List<DailyTaskDto> = emptyList(),
    val isGenerating: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            tasksRepository.getTodayTask().onSuccess { task ->
                _uiState.update { it.copy(todayTask = task) }
            }

            tasksRepository.getTasks().onSuccess { paginated ->
                _uiState.update { it.copy(taskHistory = paginated.results) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun generateTask() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true) }
            tasksRepository.generateTask().onSuccess { task ->
                _uiState.update { it.copy(todayTask = task, isGenerating = false) }
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message, isGenerating = false) }
            }
        }
    }

    fun completeTask(id: String) {
        viewModelScope.launch {
            tasksRepository.completeTask(id).onSuccess { task ->
                _uiState.update { it.copy(todayTask = task) }
                load()
            }
        }
    }
}
