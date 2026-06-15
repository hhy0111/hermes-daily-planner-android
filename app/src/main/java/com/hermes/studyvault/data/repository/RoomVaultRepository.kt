package com.hermes.studyvault.data.repository

import com.hermes.studyvault.data.local.StudyVaultDatabase
import com.hermes.studyvault.data.local.entity.DeadlineEntity
import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.data.local.entity.ReviewCardEntity
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

    override suspend fun getSourcesOnce(): List<SourceEntity> = database.sourceDao().getAllOnce()

    override suspend fun getEvidenceBlocksOnce(): List<EvidenceBlockEntity> {
        return database.evidenceBlockDao().getAllOnce()
    }

    override suspend fun getNotesOnce(): List<NoteEntity> {
        return database.noteDao().getAllOnce()
    }

    override suspend fun getDeadlinesOnce(): List<DeadlineEntity> {
        return database.deadlineDao().getAllOnce()
    }

    override suspend fun getReviewCardsOnce(): List<ReviewCardEntity> {
        return database.reviewCardDao().getAllOnce()
    }
}
