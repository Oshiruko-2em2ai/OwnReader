package com.ownreader.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "pages",
    foreignKeys = [
        ForeignKey(
            entity = Comic::class,
            parentColumns = ["id"],
            childColumns = ["comicId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Page(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val comicId: Long,
    val pageNumber: Int,
    val imagePath: String,
    val width: Int = 0,
    val height: Int = 0
)
