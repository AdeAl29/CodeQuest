package com.codequest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codequest.data.model.AdaptiveRecommendation
import com.codequest.data.model.ModuleCardState
import com.codequest.data.repository.LearningRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ChallengeUiState(
    val loading: Boolean = true,
    val recommendation: AdaptiveRecommendation? = null,
    val modules: List<ModuleCardState> = emptyList()
)

class ChallengeViewModel(
    repository: LearningRepository
) : ViewModel() {
    val uiState: StateFlow<ChallengeUiState> = combine(
        repository.observeRecommendation(),
        repository.observeModuleCards()
    ) { recommendation, modules ->
        ChallengeUiState(
            loading = false,
            recommendation = recommendation,
            modules = modules
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ChallengeUiState()
    )
}
