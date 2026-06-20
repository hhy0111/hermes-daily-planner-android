package com.hermes.studyvault.domain.planner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant

class PlannerItemFactoryTest {
    @Test
    fun createsUnreadTaskWithTrimmedTitleAndDetails() {
        val now = Instant.parse("2026-06-20T06:30:00Z")

        val task = PlannerItemFactory.createTask(
            id = "task-1",
            title = "  Review release checklist  ",
            details = "  Confirm store listing and signed AAB.  ",
            now = now,
        )

        assertEquals("task-1", task.id)
        assertEquals("Review release checklist", task.title)
        assertEquals("task", task.type)
        assertEquals("Confirm store listing and signed AAB.", task.capturedText)
        assertEquals("unread", task.status)
        assertEquals(now, task.createdAt)
        assertEquals(now, task.updatedAt)
        assertNull(task.url)
        assertNull(task.localFilePath)
        assertNull(task.collectionId)
    }

    @Test
    fun createsNoteWithParagraphBodyJson() {
        val now = Instant.parse("2026-06-20T07:10:00Z")

        val note = PlannerItemFactory.createNote(
            id = "note-1",
            title = "  Launch notes  ",
            body = "  Watch first week retention.  ",
            now = now,
        )

        assertEquals("note-1", note.id)
        assertEquals("Launch notes", note.title)
        assertEquals("""[{"type":"paragraph","text":"Watch first week retention."}]""", note.bodyBlocksJson)
        assertEquals("", note.tagsCsv)
        assertEquals(now, note.createdAt)
        assertEquals(now, note.updatedAt)
        assertNull(note.collectionId)
    }
}
