package com.codequest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codequest.data.model.LearningGoal
import com.codequest.data.model.StartLevel
import com.codequest.data.repository.LearningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val name: String = "",
    val goal: LearningGoal = LearningGoal.FROM_ZERO,
    val startLevel: StartLevel = StartLevel.BEGINNER,
    val saving: Boolean = false
)

class OnboardingViewModel(
    private val repository: LearningRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun selectGoal(goal: LearningGoal) {
        _uiState.update { it.copy(goal = goal) }
    }

    fun selectStartLevel(startLevel: StartLevel) {
        _uiState.update { it.copy(startLevel = startLevel) }
    }

    fun complete(onDone: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(saving = true) }
            val state = uiState.value
            repository.completeOnboarding(state.name, state.goal, state.startLevel)
            _uiState.update { it.copy(saving = false) }
            onDone()
        }
    }
}
