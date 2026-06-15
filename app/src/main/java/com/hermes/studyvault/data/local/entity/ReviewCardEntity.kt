package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "review_cards")
data class ReviewCardEntity(
    @PrimaryKey val id: String,
    val front: String,
    val back: String,
    val sourceEvidenceBlockId: String?,
    val sourceNoteId: String?,
    val nextReviewAt: Instant,
    val difficulty: Int,
    val reviewCount: Int,
    val lastReviewedAt: Instant?
)
