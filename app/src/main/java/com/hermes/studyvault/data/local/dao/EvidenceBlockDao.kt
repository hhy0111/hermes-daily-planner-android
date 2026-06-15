package com.hermes.studyvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EvidenceBlockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(evidenceBlock: EvidenceBlockEntity)

    @Query("SELECT * FROM evidence_blocks ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<EvidenceBlockEntity>>

    @Query("SELECT * FROM evidence_blocks ORDER BY createdAt DESC")
    suspend fun getAllOnce(): List<EvidenceBlockEntity>
}
