package com.codequest.domain.usecase

import com.codequest.data.repository.LearningRepository

class SubmitQuizUseCase(private val repository: LearningRepository) {
    suspend operator fun invoke(
        moduleId: String,
        selectedAnswers: Map<String, String>,
        durationSeconds: Long
    ) = repository.submitQuiz(moduleId, selectedAnswers, durationSeconds)
}
