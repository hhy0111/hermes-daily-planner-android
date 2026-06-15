package com.hermes.studyvault.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hermes.studyvault.data.local.dao.DeadlineDao
import com.hermes.studyvault.data.local.dao.EvidenceBlockDao
import com.hermes.studyvault.data.local.dao.NoteDao
import com.hermes.studyvault.data.local.dao.ReviewCardDao
import com.hermes.studyvault.data.local.dao.SourceDao
import com.hermes.studyvault.data.local.entity.CollectionEntity
import com.hermes.studyvault.data.local.entity.DeadlineEntity
import com.hermes.studyvault.data.local.entity.EvidenceBlockEntity
import com.hermes.studyvault.data.local.entity.NoteEntity
import com.hermes.studyvault.data.local.entity.ReviewCardEntity
import com.hermes.studyvault.data.local.entity.SourceEntity
import com.hermes.studyvault.data.local.entity.TagEntity

@Database(
    entities = [
        SourceEntity::class,
        EvidenceBlockEntity::class,
        NoteEntity::class,
        DeadlineEntity::class,
        ReviewCardEntity::class,
        TagEntity::class,
        CollectionEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class StudyVaultDatabase : RoomDatabase() {
    abstract fun sourceDao(): SourceDao
    abstract fun evidenceBlockDao(): EvidenceBlockDao
    abstract fun noteDao(): NoteDao
    abstract fun deadlineDao(): DeadlineDao
    abstract fun reviewCardDao(): ReviewCardDao
}
