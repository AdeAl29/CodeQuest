package com.codequest

import android.app.Application
import com.codequest.data.local.CodeQuestDatabase
import com.codequest.data.local.UserPreferencesDataSource
import com.codequest.data.repository.LearningRepository
import com.codequest.domain.algorithm.AdaptiveLearningEngine
import com.codequest.domain.usecase.ObserveDashboardUseCase
import com.codequest.domain.usecase.SubmitQuizUseCase

class CodeQuestApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

class AppContainer(application: Application) {
    private val database = CodeQuestDatabase.getInstance(application)
    private val adaptiveLearningEngine = AdaptiveLearningEngine()
    private val userPreferencesDataSource = UserPreferencesDataSource(application)

    val learningRepository = LearningRepository(
        progressDao = database.progressDao(),
        userPreferencesDataSource = userPreferencesDataSource,
        adaptiveLearningEngine = adaptiveLearningEngine
    )

    val observeDashboardUseCase = ObserveDashboardUseCase(learningRepository)
    val submitQuizUseCase = SubmitQuizUseCase(learningRepository)
}
