package com.hermes.studyvault.domain.export

import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity

object MarkdownExporter {
    fun exportNote(note: NoteEntity, evidenceBlocks: List<EvidenceBlockEntity>): String {
        val bodyText = note.bodyBlocksJson
            .replace("[", "")
            .replace("]", "")
            .replace("{", "")
            .replace("}", "")
            .replace("\"type\":\"paragraph\",", "")
            .replace("\"text\":\"", "")
            .replace("\"", "")

        val evidenceMarkdown = evidenceBlocks
            .filter { evidence -> evidence.linkedNoteIdsCsv.split(",").map { it.trim() }.contains(note.id) }
            .joinToString(separator = "\n\n") { evidence ->
                buildString {
                    appendLine("> ${evidence.quoteText}")
                    if (evidence.userThought.isNotBlank()) appendLine()
                    if (evidence.userThought.isNotBlank()) appendLine(evidence.userThought)
                    if (!evidence.sourceLocation.isNullOrBlank()) appendLine("Source location: ${evidence.sourceLocation}")
                }.trim()
            }

        return buildString {
            appendLine("# ${note.title}")
            appendLine()
            appendLine(bodyText)
            if (evidenceMarkdown.isNotBlank()) {
                appendLine()
                appendLine("## Evidence")
                appendLine()
                appendLine(evidenceMarkdown)
            }
        }.trimEnd()
    }
}
