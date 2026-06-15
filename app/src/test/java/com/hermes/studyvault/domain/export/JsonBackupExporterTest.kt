package com.hermes.studyvault.domain.export

import com.hermes.studyvault.data.local.entity.SourceEntity
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class JsonBackupExporterTest {
    @Test
    fun backupIncludesSourceFields() {
        val now = Instant.parse("2026-06-16T00:00:00Z")
        val source = SourceEntity("source-1", "Article", "web", "https://example.com", null, "text", "unread", null, now, now)

        val json = JsonBackupExporter.exportSources(listOf(source))

        assertTrue(json.contains("\"id\":\"source-1\""))
        assertTrue(json.contains("\"title\":\"Article\""))
        assertTrue(json.contains("\"url\":\"https://example.com\""))
    }
}
