package com.hermes.studyvault.domain.search

import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.data.repository.VaultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class SearchVaultUseCaseTest {
    @Test
    fun searchFindsSourcesEvidenceAndNotesCaseInsensitively() = runTest {
        val repository = FakeVaultRepository()
        val useCase = SearchVaultUseCase(repository)

        val result = useCase.search("retrieval")

        assertEquals(
            listOf("source:source-1", "evidence:evidence-1", "note:note-1"),
            result.map { "${it.type}:${it.id}" }
        )
    }

    private class FakeVaultRepository : VaultRepository {
        private val now = Instant.parse("2026-06-16T00:00:00Z")

        override suspend fun insertSource(source: SourceEntity) = Unit
        override fun observeSources(): Flow<List<SourceEntity>> = flowOf(emptyList())

        override suspend fun getSourcesOnce(): List<SourceEntity> = listOf(
            SourceEntity("source-1", "Retrieval practice", "web", null, null, null, "unread", null, now, now)
        )

        override suspend fun getEvidenceBlocksOnce(): List<EvidenceBlockEntity> = listOf(
            EvidenceBlockEntity("evidence-1", "source-1", "retrieval improves memory", "", "p4", "", "", "", "", now, now)
        )

        override suspend fun getNotesOnce(): List<NoteEntity> = listOf(
            NoteEntity("note-1", "Exam plan", """[{"text":"Use retrieval cards"}]""", null, "study", now, now)
        )
    }
}
