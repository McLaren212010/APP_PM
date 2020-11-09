package com.example.app_pm.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.app_pm.entities.Note

@Dao
interface NoteDao{

    @Query("SELECT * FROM note_table ORDER BY note ASC")
    fun getAlphabetizedWords(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM note_table where priority == :priority ")
    fun getNotePriority(priority: String): LiveData<Note>

    @Query("DELETE FROM note_table where note == :note")
    suspend fun deleteByNote(note: String)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun  delete (note: Note)
}
