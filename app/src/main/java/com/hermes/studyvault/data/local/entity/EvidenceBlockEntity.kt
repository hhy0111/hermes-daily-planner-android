package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "evidence_blocks")
data class EvidenceBlockEntity(
    @PrimaryKey val id: String,
    val sourceId: String?,
    val quoteText: String,
    val userThought: String,
    val sourceLocation: String?,
    val tagsCsv: String,
    val linkedNoteIdsCsv: String,
    val linkedDeadlineIdsCsv: String,
    val linkedReviewCardIdsCsv: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
