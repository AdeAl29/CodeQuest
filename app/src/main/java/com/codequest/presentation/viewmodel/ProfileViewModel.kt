package com.codequest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codequest.data.model.BadgeProgress
import com.codequest.data.model.LearningStats
import com.codequest.data.model.UserPreferences
import com.codequest.data.repository.LearningRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ProfileUiState(
    val loading: Boolean = true,
    val user: UserPreferences = UserPreferences(),
    val stats: LearningStats = LearningStats(),
    val badges: List<BadgeProgress> = emptyList()
)

class ProfileViewModel(
    repository: LearningRepository
) : ViewModel() {
    val uiState: StateFlow<ProfileUiState> = combine(
        repository.userPreferences,
        repository.observeStats(),
        repository.observeBadges()
    ) { user, stats, badges ->
        ProfileUiState(
            loading = false,
            user = user,
            stats = stats,
            badges = badges
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState()
    )
}
