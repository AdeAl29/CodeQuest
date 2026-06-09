package com.codequest.domain.usecase

import com.codequest.data.repository.LearningRepository

class ObserveDashboardUseCase(private val repository: LearningRepository) {
    operator fun invoke() = repository.observeDashboard()
}
