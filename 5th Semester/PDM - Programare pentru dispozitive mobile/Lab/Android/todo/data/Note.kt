package com.example.smartnote.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val id: String = "",
    val titlu: String = "",
    val descriere: String = "",
    val data: String = "",
    val prioritate: Int = -1,
    val complet: Boolean = false,
    val userID: String? = null,
    var isSynced: Boolean = false,
    val imageUri: String? = null
)
