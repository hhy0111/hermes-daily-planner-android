package com.hermes.studyvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hermes.studyvault.data.local.entity.DeadlineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeadlineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deadline: DeadlineEntity)

    @Query("SELECT * FROM deadlines ORDER BY dueAt ASC")
    fun observeAll(): Flow<List<DeadlineEntity>>

    @Query("SELECT * FROM deadlines ORDER BY dueAt ASC")
    suspend fun getAllOnce(): List<DeadlineEntity>
}
