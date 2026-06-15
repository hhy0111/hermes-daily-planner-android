package com.hermes.studyvault.domain.share

import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.DeadlineEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.data.local.entity.ReviewCardEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.data.repository.VaultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class CreateSourceFromShareUseCaseTest {
    @Test
    fun textShareCreatesUnreadTextSource() = runTest {
        val repository = FakeVaultRepository()
        val clock = Clock.fixed(Instant.parse("2026-06-16T00:00:00Z"), ZoneOffset.UTC)
        val useCase = CreateSourceFromShareUseCase(repository, clock)

        val source = useCase.create(
            SharedPayload.Text(
                text = "https://example.com\nImportant article",
                subject = "Example Article"
            )
        )

        assertNotNull(source.id)
        assertEquals("Example Article", source.title)
        assertEquals("text", source.type)
        assertEquals("https://example.com", source.url)
        assertEquals("unread", source.status)
        assertEquals(source, repository.insertedSource)
    }

    private class FakeVaultRepository : VaultRepository {
        var insertedSource: SourceEntity? = null

        override suspend fun insertSource(source: SourceEntity) {
            insertedSource = source
        }

        override fun observeSources(): Flow<List<SourceEntity>> = emptyFlow()

        override suspend fun getSourcesOnce(): List<SourceEntity> = emptyList()

        override suspend fun getEvidenceBlocksOnce(): List<EvidenceBlockEntity> = emptyList()

        override suspend fun getNotesOnce(): List<NoteEntity> = emptyList()

        override suspend fun getDeadlinesOnce(): List<DeadlineEntity> = emptyList()

        override suspend fun getReviewCardsOnce(): List<ReviewCardEntity> = emptyList()
    }
}
