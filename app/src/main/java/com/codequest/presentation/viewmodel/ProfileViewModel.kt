package com.codequest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codequest.data.model.BadgeProgress
import com.codequest.data.model.LearningGoal
import com.codequest.data.model.LearningStats
import com.codequest.data.model.QuizAttemptSummary
import com.codequest.data.model.StartLevel
import com.codequest.data.model.UserPreferences
import com.codequest.data.repository.LearningRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val loading: Boolean = true,
    val user: UserPreferences = UserPreferences(),
    val stats: LearningStats = LearningStats(),
    val badges: List<BadgeProgress> = emptyList(),
    val recentAttempts: List<QuizAttemptSummary> = emptyList()
)

class ProfileViewModel(
    private val repository: LearningRepository
) : ViewModel() {
    val uiState: StateFlow<ProfileUiState> = combine(
        repository.userPreferences,
        repository.observeStats(),
        repository.observeBadges(),
        repository.observeRecentAttempts(limit = 5)
    ) { user, stats, badges, recentAttempts ->
        ProfileUiState(
            loading = false,
            user = user,
            stats = stats,
            badges = badges,
            recentAttempts = recentAttempts
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState()
    )

    fun updateProfile(name: String, goal: LearningGoal, startLevel: StartLevel) {
        viewModelScope.launch {
            repository.updateProfile(name, goal, startLevel)
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
        }
    }
}
