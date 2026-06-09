package com.codequest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codequest.data.model.BadgeProgress
import com.codequest.data.repository.LearningRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class AchievementUiState(
    val loading: Boolean = true,
    val badges: List<BadgeProgress> = emptyList()
)

class AchievementViewModel(
    repository: LearningRepository
) : ViewModel() {
    val uiState: StateFlow<AchievementUiState> = repository.observeBadges()
        .map { AchievementUiState(loading = false, badges = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AchievementUiState()
        )
}
