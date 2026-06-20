package com.hermes.studyvault.domain.planner

import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import java.time.Instant

object PlannerItemFactory {
    fun createTask(
        id: String,
        title: String,
        details: String,
        now: Instant,
    ): SourceEntity {
        return SourceEntity(
            id = id,
            title = title.trim(),
            type = "task",
            url = null,
            localFilePath = null,
            capturedText = details.trim(),
            status = "unread",
            collectionId = null,
            createdAt = now,
            updatedAt = now,
        )
    }

    fun createNote(
        id: String,
        title: String,
        body: String,
        now: Instant,
    ): NoteEntity {
        return NoteEntity(
            id = id,
            title = title.trim(),
            bodyBlocksJson = """[{"type":"paragraph","text":"${body.trim().escapeJson()}"}]""",
            collectionId = null,
            tagsCsv = "",
            createdAt = now,
            updatedAt = now,
        )
    }

    private fun String.escapeJson(): String {
        return replace("\\", "\\\\").replace("\"", "\\\"")
    }
}
