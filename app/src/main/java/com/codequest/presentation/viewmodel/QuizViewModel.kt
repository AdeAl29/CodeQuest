package com.codequest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.codequest.AppContainer
import com.codequest.data.model.Difficulty
import com.codequest.data.model.LessonModule
import com.codequest.data.model.QuizQuestion
import com.codequest.data.model.QuizResult
import com.codequest.data.repository.LearningRepository
import com.codequest.domain.usecase.SubmitQuizUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizUiState(
    val loading: Boolean = true,
    val module: LessonModule? = null,
    val difficulty: Difficulty = Difficulty.EASY,
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswers: Map<String, String> = emptyMap(),
    val result: QuizResult? = null,
    val submitting: Boolean = false
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentIndex)

    val currentAnswer: String?
        get() = currentQuestion?.let { selectedAnswers[it.id] }

    val isLastQuestion: Boolean
        get() = currentIndex == questions.lastIndex
}

class QuizViewModel(
    private val moduleId: String,
    private val repository: LearningRepository,
    private val submitQuizUseCase: SubmitQuizUseCase
) : ViewModel() {
    private val startedAt = System.currentTimeMillis()
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.markLessonStarted(moduleId)
            val recommendation = repository.currentRecommendation()
            val module = repository.getModule(moduleId)
            val questions = repository.getQuestionsForModule(moduleId, recommendation.difficulty)
            _uiState.update {
                it.copy(
                    loading = false,
                    module = module,
                    difficulty = recommendation.difficulty,
                    questions = questions
                )
            }
        }
    }

    fun selectAnswer(answer: String) {
        val state = uiState.value
        val question = state.currentQuestion ?: return
        if (state.selectedAnswers.containsKey(question.id)) return
        _uiState.update {
            it.copy(selectedAnswers = it.selectedAnswers + (question.id to answer))
        }
    }

    fun next() {
        val state = uiState.value
        if (state.currentQuestion == null || state.currentAnswer == null) return

        if (state.isLastQuestion) {
            submit()
        } else {
            _uiState.update { it.copy(currentIndex = it.currentIndex + 1) }
        }
    }

    private fun submit() {
        viewModelScope.launch {
            _uiState.update { it.copy(submitting = true) }
            val durationSeconds = ((System.currentTimeMillis() - startedAt) / 1000L).coerceAtLeast(1L)
            val result = submitQuizUseCase(
                moduleId,
                uiState.value.selectedAnswers,
                durationSeconds
            )
            _uiState.update { it.copy(result = result, submitting = false) }
        }
    }

    companion object {
        fun factory(container: AppContainer, moduleId: String): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return QuizViewModel(
                        moduleId = moduleId,
                        repository = container.learningRepository,
                        submitQuizUseCase = container.submitQuizUseCase
                    ) as T
                }
            }
        }
    }
}
