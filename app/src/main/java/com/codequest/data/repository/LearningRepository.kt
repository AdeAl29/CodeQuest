package com.codequest.data.repository

import com.codequest.data.local.BadgeEntity
import com.codequest.data.local.DummyLearningContent
import com.codequest.data.local.ModuleProgressEntity
import com.codequest.data.local.ProgressDao
import com.codequest.data.local.QuizAttemptEntity
import com.codequest.data.local.UserPreferencesDataSource
import com.codequest.data.model.AdaptiveInput
import com.codequest.data.model.AdaptiveRecommendation
import com.codequest.data.model.BadgeProgress
import com.codequest.data.model.DashboardData
import com.codequest.data.model.Difficulty
import com.codequest.data.model.LearningGoal
import com.codequest.data.model.LearningStats
import com.codequest.data.model.LessonModule
import com.codequest.data.model.ModuleCardState
import com.codequest.data.model.ModuleProgress
import com.codequest.data.model.ModuleStatus
import com.codequest.data.model.QuizAttemptSummary
import com.codequest.data.model.QuizQuestion
import com.codequest.data.model.QuizResult
import com.codequest.data.model.StartLevel
import com.codequest.data.model.UserPreferences
import com.codequest.domain.algorithm.AdaptiveLearningEngine
import kotlin.math.max
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class LearningRepository(
    private val progressDao: ProgressDao,
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val adaptiveLearningEngine: AdaptiveLearningEngine
) {
    val userPreferences: Flow<UserPreferences> = userPreferencesDataSource.userPreferences
    val modules: List<LessonModule> = DummyLearningContent.modules
    val questions: List<QuizQuestion> = DummyLearningContent.questions

    fun observeModuleCards(): Flow<List<ModuleCardState>> {
        return progressDao.observeModuleProgress().map { entities ->
            buildModuleCards(entities.map { it.toDomain() })
        }
    }

    fun observeModuleProgress(moduleId: String): Flow<ModuleProgress?> {
        return progressDao.observeProgress(moduleId).map { it?.toDomain() }
    }

    fun observeBadges(): Flow<List<BadgeProgress>> {
        return progressDao.observeBadges().map { unlocked ->
            val unlockedById = unlocked.associateBy { it.badgeId }
            DummyLearningContent.badges.map { badge ->
                BadgeProgress(
                    badge = badge,
                    unlockedAt = unlockedById[badge.id]?.unlockedAt
                )
            }
        }
    }

    fun observeRecentAttempts(limit: Int = 5): Flow<List<QuizAttemptSummary>> {
        return progressDao.observeQuizAttempts().map { entities ->
            entities.take(limit).map { entity ->
                val moduleTitle = getModule(entity.moduleId)?.title ?: entity.moduleId
                QuizAttemptSummary(
                    id = entity.id,
                    moduleId = entity.moduleId,
                    moduleTitle = moduleTitle,
                    score = entity.score,
                    correctAnswers = entity.correctAnswers,
                    totalQuestions = entity.totalQuestions,
                    durationSeconds = entity.durationSeconds,
                    completedAt = entity.completedAt
                )
            }
        }
    }

    fun observeStats(): Flow<LearningStats> {
        return combine(
            progressDao.observeModuleProgress(),
            progressDao.observeQuizAttempts(),
            progressDao.observeBadges()
        ) { progress, attempts, badges ->
            buildStats(progress.map { it.toDomain() }, attempts, badges.size)
        }
    }

    fun observeRecommendation(): Flow<AdaptiveRecommendation> {
        return combine(
            userPreferences,
            progressDao.observeModuleProgress(),
            progressDao.observeQuizAttempts()
        ) { user, progress, attempts ->
            buildRecommendation(
                user = user,
                progress = progress.map { it.toDomain() },
                attempts = attempts,
                currentModuleId = attempts.firstOrNull()?.moduleId
            )
        }
    }

    fun observeDashboard(): Flow<DashboardData> {
        return combine(
            userPreferences,
            observeStats(),
            observeRecommendation(),
            observeModuleCards(),
            observeRecentAttempts(limit = 4)
        ) { user, stats, recommendation, moduleCards, recentAttempts ->
            DashboardData(
                user = user,
                stats = stats,
                recommendation = recommendation,
                moduleCards = moduleCards,
                recentAttempts = recentAttempts
            )
        }
    }

    fun getModule(moduleId: String): LessonModule? = modules.firstOrNull { it.id == moduleId }

    suspend fun completeOnboarding(name: String, goal: LearningGoal, startLevel: StartLevel) {
        userPreferencesDataSource.completeOnboarding(name, goal, startLevel)
    }

    suspend fun updateProfile(name: String, goal: LearningGoal, startLevel: StartLevel) {
        userPreferencesDataSource.updateProfile(name, goal, startLevel)
    }

    suspend fun resetAllProgress() {
        progressDao.clearModuleProgress()
        progressDao.clearQuizAttempts()
        progressDao.clearBadges()
        userPreferencesDataSource.resetUserProgress()
    }

    fun getQuestionsForModule(moduleId: String, difficulty: Difficulty): List<QuizQuestion> {
        val moduleQuestions = questions.filter { it.moduleId == moduleId }
        val preferred = moduleQuestions.filter { it.difficulty == difficulty }
        val review = when (difficulty) {
            Difficulty.EASY -> emptyList()
            Difficulty.MEDIUM -> moduleQuestions.filter { it.difficulty == Difficulty.EASY }
            Difficulty.HARD -> moduleQuestions.filter { it.difficulty != Difficulty.HARD }
        }

        return (preferred + review + moduleQuestions)
            .distinctBy { it.id }
            .take(5)
    }

    suspend fun markLessonStarted(moduleId: String) {
        val existing = progressDao.getProgress(moduleId)
        if (existing == null) {
            progressDao.upsertProgress(
                ModuleProgressEntity(
                    moduleId = moduleId,
                    status = ModuleStatus.IN_PROGRESS.name
                )
            )
        }
    }

    suspend fun markLessonUnderstood(moduleId: String): Boolean {
        val existing = progressDao.getProgress(moduleId)
        return if (existing == null) {
            progressDao.upsertProgress(
                ModuleProgressEntity(
                    moduleId = moduleId,
                    status = ModuleStatus.IN_PROGRESS.name
                )
            )
            userPreferencesDataSource.updateDailyStreak()
            userPreferencesDataSource.addXp(15)
            refreshBadges()
            true
        } else if (existing.status == ModuleStatus.AVAILABLE.name) {
            progressDao.upsertProgress(
                existing.copy(
                    status = ModuleStatus.IN_PROGRESS.name,
                    updatedAt = System.currentTimeMillis()
                )
            )
            userPreferencesDataSource.updateDailyStreak()
            userPreferencesDataSource.addXp(15)
            refreshBadges()
            true
        } else {
            userPreferencesDataSource.updateDailyStreak()
            false
        }
    }

    suspend fun submitQuiz(
        moduleId: String,
        selectedAnswers: Map<String, String>,
        durationSeconds: Long
    ): QuizResult {
        val answeredQuestions = questions.filter { selectedAnswers.containsKey(it.id) }
        val correct = answeredQuestions.count { question ->
            selectedAnswers[question.id] == question.correctAnswer
        }
        val wrong = answeredQuestions.size - correct
        val score = if (answeredQuestions.isEmpty()) 0 else (correct * 100) / answeredQuestions.size
        val weakTopic = answeredQuestions
            .filter { selectedAnswers[it.id] != it.correctAnswer }
            .groupingBy { it.topic }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
        val xpEarned = calculateXp(score, correct)

        progressDao.insertQuizAttempt(
            QuizAttemptEntity(
                moduleId = moduleId,
                score = score,
                totalQuestions = answeredQuestions.size,
                correctAnswers = correct,
                wrongAnswers = wrong,
                durationSeconds = durationSeconds,
                weakTopics = weakTopic.orEmpty()
            )
        )

        val existing = progressDao.getProgress(moduleId)
        val alreadyCompleted = existing?.status == ModuleStatus.COMPLETED.name
        val nextStatus = if (score >= 60 || alreadyCompleted) {
            ModuleStatus.COMPLETED
        } else {
            ModuleStatus.IN_PROGRESS
        }
        progressDao.upsertProgress(
            ModuleProgressEntity(
                moduleId = moduleId,
                status = nextStatus.name,
                bestScore = max(existing?.bestScore ?: 0, score),
                attempts = (existing?.attempts ?: 0) + 1,
                correctAnswers = (existing?.correctAnswers ?: 0) + correct,
                wrongAnswers = (existing?.wrongAnswers ?: 0) + wrong,
                completedAt = if (score >= 60) {
                    existing?.completedAt ?: System.currentTimeMillis()
                } else {
                    existing?.completedAt
                },
                updatedAt = System.currentTimeMillis()
            )
        )

        userPreferencesDataSource.updateDailyStreak()
        val (previousLevel, currentLevel) = userPreferencesDataSource.addXp(xpEarned)
        refreshBadges()

        val user = userPreferencesDataSource.currentUser()
        val progress = progressDao.getAllProgress().map { it.toDomain() }
        val attempts = progressDao.getQuizAttempts()
        val recommendation = buildRecommendation(user, progress, attempts, moduleId)

        return QuizResult(
            moduleId = moduleId,
            score = score,
            correctAnswers = correct,
            wrongAnswers = wrong,
            xpEarned = xpEarned,
            recommendation = recommendation,
            leveledUp = currentLevel > previousLevel
        )
    }

    suspend fun currentRecommendation(): AdaptiveRecommendation {
        return buildRecommendation(
            user = userPreferencesDataSource.currentUser(),
            progress = progressDao.getAllProgress().map { it.toDomain() },
            attempts = progressDao.getQuizAttempts(),
            currentModuleId = progressDao.getQuizAttempts().firstOrNull()?.moduleId
        )
    }

    private fun buildModuleCards(progress: List<ModuleProgress>): List<ModuleCardState> {
        val progressById = progress.associateBy { it.moduleId }
        return modules.map { module ->
            val stored = progressById[module.id]
            val previousCompleted = module.order == 1 || modules
                .filter { it.order < module.order }
                .all { progressById[it.id]?.status == ModuleStatus.COMPLETED }

            val status = when {
                stored?.status == ModuleStatus.COMPLETED -> ModuleStatus.COMPLETED
                stored?.status == ModuleStatus.IN_PROGRESS -> ModuleStatus.IN_PROGRESS
                previousCompleted -> ModuleStatus.AVAILABLE
                else -> ModuleStatus.LOCKED
            }

            ModuleCardState(
                module = module,
                status = status,
                bestScore = stored?.bestScore ?: 0,
                attempts = stored?.attempts ?: 0
            )
        }
    }

    private fun buildStats(
        progress: List<ModuleProgress>,
        attempts: List<QuizAttemptEntity>,
        unlockedBadgeCount: Int
    ): LearningStats {
        val completedModules = progress.count { it.status == ModuleStatus.COMPLETED }
        val completionRate = if (modules.isEmpty()) 0.0 else (completedModules.toDouble() / modules.size) * 100.0
        val averageScore = attempts.takeIf { it.isNotEmpty() }?.map { it.score }?.average()?.toInt() ?: 0

        return LearningStats(
            completedModules = completedModules,
            totalModules = modules.size,
            completedQuizzes = attempts.size,
            unlockedBadges = unlockedBadgeCount,
            totalBadges = DummyLearningContent.badges.size,
            averageScore = averageScore,
            completionRate = completionRate
        )
    }

    private fun buildRecommendation(
        user: UserPreferences,
        progress: List<ModuleProgress>,
        attempts: List<QuizAttemptEntity>,
        currentModuleId: String?
    ): AdaptiveRecommendation {
        val latestAttempt = attempts.firstOrNull()
        val completedModules = progress.count { it.status == ModuleStatus.COMPLETED }
        val completionRate = if (modules.isEmpty()) 0.0 else (completedModules.toDouble() / modules.size) * 100.0
        val input = AdaptiveInput(
            quizScore = latestAttempt?.score?.toDouble() ?: user.startLevel.baselineScore.toDouble(),
            attempts = latestAttempt?.let { latest ->
                attempts.count { it.moduleId == latest.moduleId }
            } ?: 0,
            durationSeconds = latestAttempt?.durationSeconds ?: 0L,
            correctAnswers = latestAttempt?.correctAnswers ?: 0,
            wrongAnswers = latestAttempt?.wrongAnswers ?: 0,
            completionRate = completionRate,
            streakDays = user.streakDays,
            weakTopic = latestAttempt?.weakTopics?.takeIf { it.isNotBlank() }
        )

        return adaptiveLearningEngine.recommendModule(
            modules = modules,
            progress = progress,
            currentModuleId = currentModuleId,
            input = input
        )
    }

    private suspend fun refreshBadges() {
        val user = userPreferencesDataSource.currentUser()
        val progress = progressDao.getAllProgress().map { it.toDomain() }
        val attempts = progressDao.getQuizAttempts()
        val progressById = progress.associateBy { it.moduleId }

        val unlockedIds = buildSet {
            if (attempts.isNotEmpty()) add("first_code")
            if (progressById["algorithm"]?.status == ModuleStatus.COMPLETED) add("algorithm_path")
            if (progressById["branching"]?.status == ModuleStatus.COMPLETED) add("logic_starter")
            if ((progressById["loops"]?.correctAnswers ?: 0) >= 5) add("loop_master")
            if (user.streakDays >= 3) add("consistent_learner")
            if (user.xp >= 500) add("xp_hunter")
            if (attempts.any { it.score >= 90 }) add("quiz_ace")
            if (progress.count { it.status == ModuleStatus.COMPLETED } == modules.size) add("path_finisher")
        }

        unlockedIds.forEach { badgeId ->
            progressDao.unlockBadge(BadgeEntity(badgeId))
        }
    }

    private fun calculateXp(score: Int, correctAnswers: Int): Int {
        val scoreBonus = when {
            score >= 90 -> 60
            score >= 75 -> 40
            score >= 60 -> 25
            else -> 10
        }
        return 25 + (correctAnswers * 12) + scoreBonus
    }
}
