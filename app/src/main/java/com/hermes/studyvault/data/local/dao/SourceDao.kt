package com.hermes.studyvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hermes.studyvault.data.local.entity.SourceEntity
import java.time.Instant
import kotlinx.coroutines.flow.Flow

@Dao
interface SourceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(source: SourceEntity)

    @Query("SELECT * FROM sources ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<SourceEntity>>

    @Query("SELECT * FROM sources ORDER BY createdAt DESC")
    suspend fun getAllOnce(): List<SourceEntity>

    @Query("SELECT * FROM sources WHERE status = :status ORDER BY createdAt DESC")
    fun observeByStatus(status: String): Flow<List<SourceEntity>>

    @Query("UPDATE sources SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: String, status: String, updatedAt: Instant)
}
