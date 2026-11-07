package com.codequest.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.codequest.data.model.LearningGoal
import com.codequest.data.model.StartLevel
import com.codequest.data.model.UserPreferences
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.codeQuestDataStore by preferencesDataStore(name = "codequest_user")

class UserPreferencesDataSource(private val context: Context) {
    private object Keys {
        val name = stringPreferencesKey("name")
        val goal = stringPreferencesKey("goal")
        val startLevel = stringPreferencesKey("start_level")
        val onboardingCompleted = booleanPreferencesKey("onboarding_completed")
        val xp = intPreferencesKey("xp")
        val level = intPreferencesKey("level")
        val streakDays = intPreferencesKey("streak_days")
        val lastStudyDate = stringPreferencesKey("last_study_date")
    }

    val userPreferences: Flow<UserPreferences> = context.codeQuestDataStore.data.map { prefs ->
        UserPreferences(
            name = prefs[Keys.name] ?: "Code Hero",
            goal = LearningGoal.fromName(prefs[Keys.goal]),
            startLevel = StartLevel.fromName(prefs[Keys.startLevel]),
            onboardingCompleted = prefs[Keys.onboardingCompleted] ?: false,
            xp = prefs[Keys.xp] ?: 0,
            level = prefs[Keys.level] ?: 1,
            streakDays = prefs[Keys.streakDays] ?: 0,
            lastStudyDate = prefs[Keys.lastStudyDate] ?: ""
        )
    }

    suspend fun completeOnboarding(name: String, goal: LearningGoal, startLevel: StartLevel) {
        context.codeQuestDataStore.edit { prefs ->
            prefs[Keys.name] = name.trim().ifBlank { "Code Hero" }
            prefs[Keys.goal] = goal.name
            prefs[Keys.startLevel] = startLevel.name
            prefs[Keys.onboardingCompleted] = true
        }
    }

    suspend fun updateProfile(name: String, goal: LearningGoal, startLevel: StartLevel) {
        context.codeQuestDataStore.edit { prefs ->
            prefs[Keys.name] = name.trim().ifBlank { "Code Hero" }
            prefs[Keys.goal] = goal.name
            prefs[Keys.startLevel] = startLevel.name
        }
    }

    suspend fun addXp(points: Int): Pair<Int, Int> {
        var previousLevel = 1
        var newLevel = 1
        context.codeQuestDataStore.edit { prefs ->
            val currentXp = prefs[Keys.xp] ?: 0
            previousLevel = prefs[Keys.level] ?: calculateLevel(currentXp)
            val updatedXp = currentXp + points
            newLevel = calculateLevel(updatedXp)
            prefs[Keys.xp] = updatedXp
            prefs[Keys.level] = newLevel
        }
        return previousLevel to newLevel
    }

    suspend fun updateDailyStreak() {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        context.codeQuestDataStore.edit { prefs ->
            val lastDate = prefs[Keys.lastStudyDate]
            if (lastDate == today.toString()) return@edit

            val currentStreak = prefs[Keys.streakDays] ?: 0
            val nextStreak = if (lastDate == yesterday.toString()) currentStreak + 1 else 1
            prefs[Keys.streakDays] = nextStreak
            prefs[Keys.lastStudyDate] = today.toString()
        }
    }

    suspend fun resetUserProgress() {
        context.codeQuestDataStore.edit { prefs ->
            prefs[Keys.xp] = 0
            prefs[Keys.level] = 1
            prefs[Keys.streakDays] = 0
            prefs[Keys.lastStudyDate] = ""
        }
    }

    suspend fun currentUser(): UserPreferences = userPreferences.first()

    private fun calculateLevel(xp: Int): Int = (xp / XP_PER_LEVEL) + 1

    companion object {
        const val XP_PER_LEVEL = 250
    }
}
