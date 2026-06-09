package com.codequest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codequest.data.model.ModuleCardState
import com.codequest.data.repository.LearningRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class LearningPathUiState(
    val loading: Boolean = true,
    val modules: List<ModuleCardState> = emptyList()
)

class LearningPathViewModel(
    repository: LearningRepository
) : ViewModel() {
    val uiState: StateFlow<LearningPathUiState> = repository.observeModuleCards()
        .map { LearningPathUiState(loading = false, modules = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LearningPathUiState()
        )
}
