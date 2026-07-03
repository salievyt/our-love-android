package dev.deocore.outlove.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.deocore.outlove.data.model.*
import dev.deocore.outlove.data.repository.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PosterUiState(
    val isLoading: Boolean = true,
    val daysTogether: Int = 0,
    val startDate: String = "",
    val completedTasks: Int = 0,
    val totalPhotos: Int = 0,
    val totalPlaces: Int = 0,
    val diaryEntryCount: Int = 0,
    val averageMood: Double = 0.0,
    val error: String? = null,
)

@HiltViewModel
class PosterGeneratorViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tasksRepository: TasksRepository,
    private val moodsRepository: MoodsRepository,
    private val diaryRepository: DiaryRepository,
    private val albumRepository: AlbumRepository,
    private val placesRepository: PlacesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PosterUiState())
    val uiState: StateFlow<PosterUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            authRepository.getRelationship().onSuccess { rel ->
                _uiState.update { it.copy(daysTogether = rel.daysTogether, startDate = rel.relationshipStartDate) }
            }

            tasksRepository.getTasks().onSuccess { paginated ->
                _uiState.update { it.copy(completedTasks = paginated.results.count { it.isCompleted }) }
            }

            albumRepository.getPhotos().onSuccess { paginated ->
                _uiState.update { it.copy(totalPhotos = paginated.count) }
            }

            placesRepository.getPlaces().onSuccess { places ->
                _uiState.update { it.copy(totalPlaces = places.size) }
            }

            diaryRepository.getEntries().onSuccess { paginated ->
                _uiState.update { it.copy(diaryEntryCount = paginated.count) }
            }

            moodsRepository.getMoodStats().onSuccess { stats ->
                _uiState.update { it.copy(averageMood = stats.average) }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
