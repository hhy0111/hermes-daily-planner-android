package com.hermes.studyvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val kind: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
