package com.dicoding.eventdicoding.ui.adapter

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.eventdicoding.data.database.Event
import com.dicoding.eventdicoding.data.database.EventDao
import com.dicoding.eventdicoding.data.database.EventDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val eventDao: EventDao = EventDatabase.getDatabase(application).eventDao()

    fun addEventToFavorites(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                eventDao.insert(event) // Menginsert event ke database
                // Anda bisa menggunakan LiveData untuk memberi tahu Fragment tentang perubahan data
            } catch (e: Exception) {
                Log.e("AddToFavorites", "Error adding event to favorites", e)
            }
        }
    }
}