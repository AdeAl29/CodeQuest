package com.codequest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.codequest.AppContainer
import com.codequest.data.model.LessonModule
import com.codequest.data.repository.LearningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LessonUiState(
    val module: LessonModule? = null,
    val marking: Boolean = false
)

class LessonViewModel(
    private val moduleId: String,
    private val repository: LearningRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        LessonUiState(module = repository.getModule(moduleId))
    )
    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.markLessonStarted(moduleId)
        }
    }

    fun markUnderstood() {
        viewModelScope.launch {
            _uiState.update { it.copy(marking = true) }
            repository.markLessonUnderstood(moduleId)
            _uiState.update { it.copy(marking = false) }
        }
    }

    companion object {
        fun factory(container: AppContainer, moduleId: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LessonViewModel(moduleId, container.learningRepository) as T
                }
            }
        }
    }
}
