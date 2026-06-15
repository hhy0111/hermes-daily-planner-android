package com.hermes.studyvault.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

@RunWith(AndroidJUnit4::class)
class StudyVaultDatabaseInstrumentedTest {
    private lateinit var db: StudyVaultDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, StudyVaultDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun sourceAndEvidenceCanBeStoredAndRead() = runBlocking {
        val now = Instant.parse("2026-06-16T00:00:00Z")
        val source = SourceEntity(
            id = "source-1",
            title = "Research Article",
            type = "web",
            url = "https://example.com/article",
            localFilePath = null,
            capturedText = "An article about learning.",
            status = "unread",
            collectionId = null,
            createdAt = now,
            updatedAt = now
        )
        val evidence = EvidenceBlockEntity(
            id = "evidence-1",
            sourceId = "source-1",
            quoteText = "Learning improves with retrieval.",
            userThought = "This supports the review feature.",
            sourceLocation = "paragraph-4",
            tagsCsv = "learning,review",
            linkedNoteIdsCsv = "",
            linkedDeadlineIdsCsv = "",
            linkedReviewCardIdsCsv = "",
            createdAt = now,
            updatedAt = now
        )

        db.sourceDao().insert(source)
        db.evidenceBlockDao().insert(evidence)

        val sources = db.sourceDao().getAllOnce()
        val evidenceBlocks = db.evidenceBlockDao().getAllOnce()

        assertEquals(listOf(source), sources)
        assertEquals(listOf(evidence), evidenceBlocks)
    }
}
