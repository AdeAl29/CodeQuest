package com.codequest.domain.algorithm

import com.codequest.data.model.AdaptiveInput
import com.codequest.data.model.AdaptiveRecommendation
import com.codequest.data.model.Difficulty
import com.codequest.data.model.LessonModule
import com.codequest.data.model.ModuleProgress
import com.codequest.data.model.ModuleStatus

class AdaptiveLearningEngine {
    fun calculateMasteryScore(input: AdaptiveInput): Double {
        val quizScore = input.quizScore.coerceIn(0.0, 100.0)
        val accuracy = input.accuracy.coerceIn(0.0, 100.0)
        val streakBonus = (input.streakDays * 10.0).coerceAtMost(100.0)
        val completionRate = input.completionRate.coerceIn(0.0, 100.0)

        // Rumus utama adaptive learning:
        // masteryScore = (quizScore * 0.5) + (accuracy * 0.3)
        //              + (streakBonus * 0.1) + (completionRate * 0.1)
        return (quizScore * 0.5) +
            (accuracy * 0.3) +
            (streakBonus * 0.1) +
            (completionRate * 0.1)
    }

    fun recommendDifficulty(masteryScore: Double): Difficulty {
        return when {
            masteryScore >= 86 -> Difficulty.HARD
            masteryScore >= 80 -> Difficulty.MEDIUM
            masteryScore >= 60 -> Difficulty.MEDIUM
            else -> Difficulty.EASY
        }
    }

    fun needsRemedial(masteryScore: Double, input: AdaptiveInput): Boolean {
        return masteryScore < 60 || input.wrongAnswers > input.correctAnswers
    }

    fun recommendModule(
        modules: List<LessonModule>,
        progress: List<ModuleProgress>,
        currentModuleId: String?,
        input: AdaptiveInput
    ): AdaptiveRecommendation {
        val masteryScore = calculateMasteryScore(input)
        val progressById = progress.associateBy { it.moduleId }
        val currentModule = currentModuleId?.let { id -> modules.firstOrNull { it.id == id } }
        val firstUnfinished = modules.firstOrNull { module ->
            progressById[module.id]?.status != ModuleStatus.COMPLETED
        } ?: modules.last()

        val remedial = needsRemedial(masteryScore, input)
        val targetModule = when {
            remedial && currentModule != null -> currentModule
            masteryScore >= 60 -> firstUnfinished
            else -> currentModule ?: firstUnfinished
        }

        val message = when {
            remedial && input.weakTopic != null ->
                "Ulangi topik ${input.weakTopic} dengan soal easy agar fondasinya lebih kuat."
            remedial ->
                "Mastery masih di bawah 60. Ulangi materi ini sebelum lanjut."
            masteryScore >= 80 ->
                "Mastery kuat. Lanjut ke materi berikutnya dengan tantangan medium/hard."
            else ->
                "Kamu bisa lanjut, tetapi sistem tetap menyisipkan beberapa soal review."
        }

        return AdaptiveRecommendation(
            module = targetModule,
            difficulty = recommendDifficulty(masteryScore),
            masteryScore = masteryScore,
            needsRemedial = remedial,
            message = message,
            weakTopic = input.weakTopic
        )
    }
}
