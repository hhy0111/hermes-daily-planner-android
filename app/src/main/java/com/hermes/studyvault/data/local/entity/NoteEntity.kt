package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val bodyBlocksJson: String,
    val collectionId: String?,
    val tagsCsv: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
