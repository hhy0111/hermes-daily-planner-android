package com.hermes.studyvault.data.repository

import com.hermes.studyvault.data.local.entity.DeadlineEntity
import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.data.local.entity.ReviewCardEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

interface VaultRepository {
    suspend fun insertSource(source: SourceEntity)
    fun observeSources(): Flow<List<SourceEntity>>
    suspend fun getSourcesOnce(): List<SourceEntity>
    suspend fun getEvidenceBlocksOnce(): List<EvidenceBlockEntity>
    suspend fun getNotesOnce(): List<NoteEntity>
    suspend fun getDeadlinesOnce(): List<DeadlineEntity>
    suspend fun getReviewCardsOnce(): List<ReviewCardEntity>
}
