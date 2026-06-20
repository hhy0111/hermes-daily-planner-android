package com.hermes.studyvault.data.local

import android.content.Context
import androidx.room.Room

object StudyVaultDatabaseProvider {
    @Volatile
    private var instance: StudyVaultDatabase? = null

    fun get(context: Context): StudyVaultDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                StudyVaultDatabase::class.java,
                "hermes-study-vault.db",
            ).build().also { instance = it }
        }
    }
}
