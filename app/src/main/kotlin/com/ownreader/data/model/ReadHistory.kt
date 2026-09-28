package com.ownreader.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "read_history",
    foreignKeys = [
        ForeignKey(
            entity = Comic::class,
            parentColumns = ["id"],
            childColumns = ["comicId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReadHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val comicId: Long,
    val lastReadPageNumber: Int = 0,
    val lastReadAt: Long = System.currentTimeMillis()
)
