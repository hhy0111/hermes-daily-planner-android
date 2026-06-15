package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "deadlines")
data class DeadlineEntity(
    @PrimaryKey val id: String,
    val title: String,
    val dueAt: Instant,
    val type: String,
    val priority: Int,
    val linkedSourceIdsCsv: String,
    val linkedNoteIdsCsv: String,
    val linkedEvidenceBlockIdsCsv: String,
    val completedAt: Instant?
)
