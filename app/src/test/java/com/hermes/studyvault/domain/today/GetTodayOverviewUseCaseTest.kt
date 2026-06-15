package com.hermes.studyvault.domain.today

import com.hermes.studyvault.data.local.entity.DeadlineEntity
import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.data.local.entity.ReviewCardEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.data.repository.VaultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class GetTodayOverviewUseCaseTest {
    @Test
    fun overviewIncludesUnreadSourcesDueReviewsAndUpcomingDeadlines() = runTest {
        val now = Instant.parse("2026-06-16T09:00:00Z")
        val overview = GetTodayOverviewUseCase(FakeVaultRepository()).get(now)

        assertEquals(1, overview.unreadSources.size)
        assertEquals(1, overview.dueReviewCards.size)
        assertEquals(1, overview.upcomingDeadlines.size)
    }

    private class FakeVaultRepository : VaultRepository {
        private val now = Instant.parse("2026-06-16T09:00:00Z")

        override suspend fun insertSource(source: SourceEntity) = Unit
        override fun observeSources(): Flow<List<SourceEntity>> = flowOf(emptyList())
        override suspend fun getSourcesOnce(): List<SourceEntity> = listOf(
            SourceEntity("source-1", "Article", "web", null, null, null, "unread", null, now, now),
            SourceEntity("source-2", "Archived", "web", null, null, null, "archived", null, now, now)
        )

        override suspend fun getEvidenceBlocksOnce(): List<EvidenceBlockEntity> = emptyList()
        override suspend fun getNotesOnce(): List<NoteEntity> = emptyList()

        override suspend fun getDeadlinesOnce(): List<DeadlineEntity> = listOf(
            DeadlineEntity("deadline-1", "Paper review", Instant.parse("2026-06-18T23:59:00Z"), "paper_review", 2, "", "", "", null)
        )

        override suspend fun getReviewCardsOnce(): List<ReviewCardEntity> = listOf(
            ReviewCardEntity("card-1", "What is retrieval?", "Active recall", null, null, Instant.parse("2026-06-16T08:00:00Z"), 2, 1, null)
        )
    }
}
