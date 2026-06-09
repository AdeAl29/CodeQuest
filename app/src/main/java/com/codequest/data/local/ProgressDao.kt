package com.codequest.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM module_progress")
    fun observeModuleProgress(): Flow<List<ModuleProgressEntity>>

    @Query("SELECT * FROM module_progress WHERE moduleId = :moduleId LIMIT 1")
    suspend fun getProgress(moduleId: String): ModuleProgressEntity?

    @Query("SELECT * FROM module_progress")
    suspend fun getAllProgress(): List<ModuleProgressEntity>

    @Upsert
    suspend fun upsertProgress(progress: ModuleProgressEntity)

    @Query("SELECT * FROM quiz_attempts ORDER BY completedAt DESC")
    fun observeQuizAttempts(): Flow<List<QuizAttemptEntity>>

    @Query("SELECT * FROM quiz_attempts ORDER BY completedAt DESC")
    suspend fun getQuizAttempts(): List<QuizAttemptEntity>

    @Insert
    suspend fun insertQuizAttempt(attempt: QuizAttemptEntity)

    @Query("SELECT * FROM badges")
    fun observeBadges(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM badges")
    suspend fun getBadges(): List<BadgeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockBadge(badge: BadgeEntity)
}
