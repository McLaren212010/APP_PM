package com.example.app_pm.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.app_pm.db.NoteDatabase
import com.example.app_pm.db.NoteRepository
import com.example.app_pm.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allNotes: LiveData<List<Note>>

    init {
        val notesDao = NoteDatabase.getDatabase(application, viewModelScope).noteDao()
        repository = NoteRepository(notesDao)
        allNotes = repository.allNotes
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun getNotePriority(priority: String) = viewModelScope.launch {
        repository.getNotePriority(priority)
    }

    /*

    // delete all
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    // delete by city
    fun deleteByCity(city: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByCity(city)
    }

    fun getCitiesByCountry(country: String): LiveData<List<City>> {
        return repository.getCitiesByCountry(country)
    }

    fun getCountryFromCity(city: String): LiveData<City> {
        return repository.getCountryFromCity(city)
    }

    fun updateCity(city: City) = viewModelScope.launch {
        repository.updateCity(city)
    }

    fun updateCountryFromCity(city: String, country: String) = viewModelScope.launch {
        repository.updateCountryFromCity(city, country)
    }
    */

}