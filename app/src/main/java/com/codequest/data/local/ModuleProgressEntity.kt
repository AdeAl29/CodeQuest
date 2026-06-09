package com.codequest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codequest.data.model.ModuleProgress
import com.codequest.data.model.ModuleStatus

@Entity(tableName = "module_progress")
data class ModuleProgressEntity(
    @PrimaryKey val moduleId: String,
    val status: String,
    val bestScore: Int = 0,
    val attempts: Int = 0,
    val correctAnswers: Int = 0,
    val wrongAnswers: Int = 0,
    val completedAt: Long? = null,
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toDomain() = ModuleProgress(
        moduleId = moduleId,
        status = ModuleStatus.valueOf(status),
        bestScore = bestScore,
        attempts = attempts,
        correctAnswers = correctAnswers,
        wrongAnswers = wrongAnswers,
        completedAt = completedAt,
        updatedAt = updatedAt
    )
}
