package com.hermes.studyvault.data.repository

import com.hermes.studyvault.data.local.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

interface VaultRepository {
    suspend fun insertSource(source: SourceEntity)
    fun observeSources(): Flow<List<SourceEntity>>
}
