package dev.deocore.outlove.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.deocore.outlove.data.model.*
import dev.deocore.outlove.data.repository.QuestionsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuestionsUiState(
    val isLoading: Boolean = true,
    val todayQuestion: QuestionDto? = null,
    val questionHistory: List<QuestionDto> = emptyList(),
    val answers: List<AnswerDto> = emptyList(),
    val isGenerating: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val questionsRepository: QuestionsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionsUiState())
    val uiState: StateFlow<QuestionsUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            questionsRepository.getTodayQuestion().onSuccess { q ->
                _uiState.update { it.copy(todayQuestion = q) }
            }

            questionsRepository.getQuestions().onSuccess { paginated ->
                _uiState.update { it.copy(questionHistory = paginated.results) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun generateQuestion() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true) }
            questionsRepository.generateQuestion().onSuccess { q ->
                _uiState.update { it.copy(todayQuestion = q, isGenerating = false) }
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message, isGenerating = false) }
            }
        }
    }

    fun answerQuestion(text: String) {
        val question = _uiState.value.todayQuestion ?: return
        viewModelScope.launch {
            questionsRepository.answerQuestion(question.id, text).onSuccess {
                load()
            }
        }
    }

    fun loadAnswers(questionId: String) {
        viewModelScope.launch {
            questionsRepository.getAnswers(questionId).onSuccess { answers ->
                _uiState.update { it.copy(answers = answers) }
            }
        }
    }
}
