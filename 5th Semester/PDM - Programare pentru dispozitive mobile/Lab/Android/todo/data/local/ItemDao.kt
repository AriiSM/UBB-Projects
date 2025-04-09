package com.example.smartnote.todo.data.local

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.smartnote.core.TAG
import com.example.smartnote.todo.data.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao {
    @Query("SELECT * FROM notes")
    fun getAll(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<Note>)

    @Update
    suspend fun update(item: Note): Int

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteById(id: String): Int

    @Query("DELETE FROM Notes")
    suspend fun deleteAll()
}
