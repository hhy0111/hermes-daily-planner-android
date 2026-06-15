package com.hermes.studyvault.domain.export

import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class MarkdownExporterTest {
    @Test
    fun noteExportIncludesEvidenceAndSourceLocation() {
        val now = Instant.parse("2026-06-16T00:00:00Z")
        val note = NoteEntity(
            id = "note-1",
            title = "Memory Study",
            bodyBlocksJson = """[{"type":"paragraph","text":"Draft body"}]""",
            collectionId = null,
            tagsCsv = "psychology",
            createdAt = now,
            updatedAt = now
        )
        val evidence = listOf(
            EvidenceBlockEntity("ev-1", "source-1", "Retrieval helps memory.", "Use this in intro.", "p.4", "memory", "note-1", "", "", now, now)
        )

        val markdown = MarkdownExporter.exportNote(note, evidence)

        assertTrue(markdown.contains("# Memory Study"))
        assertTrue(markdown.contains("Draft body"))
        assertTrue(markdown.contains("> Retrieval helps memory."))
        assertTrue(markdown.contains("Source location: p.4"))
    }
}
