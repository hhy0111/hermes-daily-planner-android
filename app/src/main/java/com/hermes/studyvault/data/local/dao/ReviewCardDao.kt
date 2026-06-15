package com.hermes.studyvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hermes.studyvault.data.local.entity.ReviewCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reviewCard: ReviewCardEntity)

    @Query("SELECT * FROM review_cards ORDER BY nextReviewAt ASC")
    fun observeAll(): Flow<List<ReviewCardEntity>>

    @Query("SELECT * FROM review_cards ORDER BY nextReviewAt ASC")
    suspend fun getAllOnce(): List<ReviewCardEntity>
}
