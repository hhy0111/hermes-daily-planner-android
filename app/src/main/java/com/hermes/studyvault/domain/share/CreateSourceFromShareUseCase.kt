package com.hermes.studyvault.domain.share

import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.data.repository.VaultRepository
import java.time.Clock
import java.util.UUID

class CreateSourceFromShareUseCase(
    private val repository: VaultRepository,
    private val clock: Clock
) {
    suspend fun create(payload: SharedPayload): SourceEntity {
        val now = clock.instant()
        val source = when (payload) {
            is SharedPayload.Text -> SourceEntity(
                id = UUID.randomUUID().toString(),
                title = payload.subject?.takeIf { it.isNotBlank() }
                    ?: payload.text.lineSequence().firstOrNull().orEmpty().ifBlank { "Text capture" },
                type = "text",
                url = payload.text.lineSequence().firstOrNull { it.startsWith("http://") || it.startsWith("https://") },
                localFilePath = null,
                capturedText = payload.text,
                status = "unread",
                collectionId = null,
                createdAt = now,
                updatedAt = now
            )
            is SharedPayload.File -> SourceEntity(
                id = UUID.randomUUID().toString(),
                title = payload.subject?.takeIf { it.isNotBlank() } ?: payload.uri.lastPathSegment ?: "Imported file",
                type = payload.mimeType ?: "file",
                url = null,
                localFilePath = payload.uri.toString(),
                capturedText = null,
                status = "unread",
                collectionId = null,
                createdAt = now,
                updatedAt = now
            )
        }
        repository.insertSource(source)
        return source
    }
}
