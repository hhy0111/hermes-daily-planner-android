package com.hermes.studyvault.domain.search

import com.hermes.studyvault.data.repository.VaultRepository

data class SearchResult(
    val id: String,
    val type: String,
    val title: String,
    val preview: String
)

class SearchVaultUseCase(
    private val repository: VaultRepository
) {
    suspend fun search(query: String): List<SearchResult> {
        val normalized = query.trim().lowercase()
        if (normalized.isEmpty()) return emptyList()

        val sourceResults = repository.getSourcesOnce()
            .filter { source ->
                source.title.contains(normalized, ignoreCase = true) ||
                    source.capturedText.orEmpty().contains(normalized, ignoreCase = true) ||
                    source.url.orEmpty().contains(normalized, ignoreCase = true)
            }
            .map { source ->
                SearchResult(
                    id = source.id,
                    type = "source",
                    title = source.title,
                    preview = source.capturedText ?: source.url ?: source.type
                )
            }

        val evidenceResults = repository.getEvidenceBlocksOnce()
            .filter { evidence ->
                evidence.quoteText.contains(normalized, ignoreCase = true) ||
                    evidence.userThought.contains(normalized, ignoreCase = true) ||
                    evidence.tagsCsv.contains(normalized, ignoreCase = true)
            }
            .map { evidence ->
                SearchResult(
                    id = evidence.id,
                    type = "evidence",
                    title = evidence.quoteText.take(48),
                    preview = evidence.userThought
                )
            }

        val noteResults = repository.getNotesOnce()
            .filter { note ->
                note.title.contains(normalized, ignoreCase = true) ||
                    note.bodyBlocksJson.contains(normalized, ignoreCase = true) ||
                    note.tagsCsv.contains(normalized, ignoreCase = true)
            }
            .map { note ->
                SearchResult(
                    id = note.id,
                    type = "note",
                    title = note.title,
                    preview = note.tagsCsv
                )
            }

        return sourceResults + evidenceResults + noteResults
    }
}
