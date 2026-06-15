package com.hermes.studyvault.data.repository

import com.hermes.studyvault.data.local.StudyVaultDatabase
import com.hermes.studyvault.data.local.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

class RoomVaultRepository(
    private val database: StudyVaultDatabase
) : VaultRepository {
    override suspend fun insertSource(source: SourceEntity) {
        database.sourceDao().insert(source)
    }

    override fun observeSources(): Flow<List<SourceEntity>> {
        return database.sourceDao().observeAll()
    }
}
