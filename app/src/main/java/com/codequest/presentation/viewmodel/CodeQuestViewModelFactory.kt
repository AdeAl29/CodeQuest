package com.codequest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codequest.AppContainer

class CodeQuestViewModelFactory(
    private val container: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(OnboardingViewModel::class.java) ->
                OnboardingViewModel(container.learningRepository) as T
            modelClass.isAssignableFrom(DashboardViewModel::class.java) ->
                DashboardViewModel(container.observeDashboardUseCase) as T
            modelClass.isAssignableFrom(LearningPathViewModel::class.java) ->
                LearningPathViewModel(container.learningRepository) as T
            modelClass.isAssignableFrom(ChallengeViewModel::class.java) ->
                ChallengeViewModel(container.learningRepository) as T
            modelClass.isAssignableFrom(AchievementViewModel::class.java) ->
                AchievementViewModel(container.learningRepository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) ->
                ProfileViewModel(container.learningRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
