package com.codequest.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ModuleProgressEntity::class,
        QuizAttemptEntity::class,
        BadgeEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class CodeQuestDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao

    companion object {
        @Volatile
        private var instance: CodeQuestDatabase? = null

        fun getInstance(context: Context): CodeQuestDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    CodeQuestDatabase::class.java,
                    "codequest.db"
                ).build().also { instance = it }
            }
        }
    }
}
