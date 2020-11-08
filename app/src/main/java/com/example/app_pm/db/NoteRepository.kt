package com.example.app_pm.db

import androidx.lifecycle.LiveData
import com.example.app_pm.dao.NoteDao
import com.example.app_pm.entities.Note

class NoteRepository(private val noteDao: NoteDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotes: LiveData<List<Note>> = noteDao.getAlphabetizedWords()


    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    fun getNotePriority(priority: String): LiveData<Note> {
        return noteDao.getNotePriority(priority)
    }
}
