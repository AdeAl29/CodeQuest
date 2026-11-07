package com.codequest.data.model

enum class LearningGoal(val label: String) {
    FROM_ZERO("Belajar dari Nol"),
    COLLEGE_PREP("Persiapan Kuliah"),
    LOGIC_PRACTICE("Latihan Logika"),
    KOTLIN_BASIC("Belajar Kotlin Dasar");

    companion object {
        fun fromName(name: String?) = entries.firstOrNull { it.name == name } ?: FROM_ZERO
    }
}

enum class StartLevel(val label: String, val baselineScore: Int) {
    BEGINNER("Pemula", 35),
    INTERMEDIATE("Menengah", 60),
    ADVANCED("Lanjutan", 78);

    companion object {
        fun fromName(name: String?) = entries.firstOrNull { it.name == name } ?: BEGINNER
    }
}

enum class ModuleStatus {
    LOCKED,
    AVAILABLE,
    IN_PROGRESS,
    COMPLETED
}

enum class Difficulty(val label: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard");

    companion object {
        fun fromName(name: String?) = entries.firstOrNull { it.name == name } ?: EASY
    }
}

enum class QuestionType(val label: String) {
    MULTIPLE_CHOICE("Pilihan Ganda"),
    TRUE_FALSE("Benar/Salah"),
    COMPLETE_CODE("Lengkapi Kode")
}

data class LessonModule(
    val id: String,
    val title: String,
    val subtitle: String,
    val order: Int,
    val estimatedMinutes: Int,
    val content: String,
    val codeExample: String,
    val codeOutput: String = "",
    val keyConcept: String
)

data class QuizQuestion(
    val id: String,
    val moduleId: String,
    val type: QuestionType,
    val difficulty: Difficulty,
    val topic: String,
    val prompt: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String
)

data class BadgeDefinition(
    val id: String,
    val title: String,
    val description: String,
    val icon: String
)

data class UserPreferences(
    val name: String = "Code Hero",
    val goal: LearningGoal = LearningGoal.FROM_ZERO,
    val startLevel: StartLevel = StartLevel.BEGINNER,
    val onboardingCompleted: Boolean = false,
    val xp: Int = 0,
    val level: Int = 1,
    val streakDays: Int = 0,
    val lastStudyDate: String = ""
)

data class ModuleProgress(
    val moduleId: String,
    val status: ModuleStatus,
    val bestScore: Int,
    val attempts: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val completedAt: Long?,
    val updatedAt: Long
)

data class BadgeProgress(
    val badge: BadgeDefinition,
    val unlockedAt: Long?
) {
    val unlocked: Boolean = unlockedAt != null
}

data class ModuleCardState(
    val module: LessonModule,
    val status: ModuleStatus,
    val bestScore: Int,
    val attempts: Int
)

data class QuizAttemptSummary(
    val id: Long,
    val moduleId: String,
    val moduleTitle: String,
    val score: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val durationSeconds: Long,
    val completedAt: Long
)

data class LearningStats(
    val completedModules: Int = 0,
    val totalModules: Int = 0,
    val completedQuizzes: Int = 0,
    val unlockedBadges: Int = 0,
    val totalBadges: Int = 0,
    val averageScore: Int = 0,
    val completionRate: Double = 0.0
)

data class DashboardData(
    val user: UserPreferences,
    val stats: LearningStats,
    val recommendation: AdaptiveRecommendation,
    val moduleCards: List<ModuleCardState>,
    val recentAttempts: List<QuizAttemptSummary> = emptyList()
)

data class AdaptiveInput(
    val quizScore: Double,
    val attempts: Int,
    val durationSeconds: Long,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val completionRate: Double,
    val streakDays: Int,
    val weakTopic: String?
) {
    val accuracy: Double
        get() {
            val total = correctAnswers + wrongAnswers
            return if (total == 0) quizScore else (correctAnswers.toDouble() / total) * 100.0
        }
}

data class AdaptiveRecommendation(
    val module: LessonModule,
    val difficulty: Difficulty,
    val masteryScore: Double,
    val needsRemedial: Boolean,
    val message: String,
    val weakTopic: String?
)

data class QuizResult(
    val moduleId: String,
    val score: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val xpEarned: Int,
    val recommendation: AdaptiveRecommendation,
    val leveledUp: Boolean
)
