package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "sources")
data class SourceEntity(
    @PrimaryKey val id: String,
    val title: String,
    val type: String,
    val url: String?,
    val localFilePath: String?,
    val capturedText: String?,
    val status: String,
    val collectionId: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)
