package com.dicoding.eventdicoding.ui.favorite

import FavoriteAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dicoding.eventdicoding.R
import com.dicoding.eventdicoding.data.database.Event
import com.dicoding.eventdicoding.data.database.EventDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.dicoding.eventdicoding.data.database.EventDao // Pastikan sudah mengimpor EventDao

class FavoriteFragment : Fragment() {

    private lateinit var eventDao: EventDao // Perbaiki deklarasi ini
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        listView = view.findViewById(R.id.listViewFavorite)

        eventDao = EventDatabase.getDatabase(requireContext()).eventDao() // Mengakses DAO dari database

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengambil data dari database dalam coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val events = eventDao.getAllFavorites()
                Log.d("FavoriteFragment", "Fetched favorite events: $events")
                setupListView(events)
            } catch (e: Exception) {
                Log.e("FavoriteFragment", "Error fetching events", e)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshFavoriteEvents() // Memanggil refresh ketika fragment resume
    }

    private fun setupListView(events: List<Event>) {
        val adapter = FavoriteAdapter(requireContext(), events) { event ->
            deleteEventFromFavorites(event) // Panggil fungsi untuk hapus event
        }
        listView.adapter = adapter
    }

    // Fungsi untuk menambahkan event ke favorit
    private fun addEventToFavorites(event: Event) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                event.isFavorite = true // Set properti isFavorite
                eventDao.insert(event)
                Log.d("AddToFavorites", "Event added to favorites: ${event.name}, ID: ${event.id}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Event ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
                    refreshFavoriteEvents() // Pastikan untuk memanggil ini
                }
            } catch (e: Exception) {
                Log.e("AddToFavorites", "Error adding event to favorites", e)
            }
        }
    }

    // Fungsi untuk menghapus event dari favorit
    private fun deleteEventFromFavorites(event: Event) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Panggil delete dengan objek event
                eventDao.delete(event) // Hapus objek event langsung
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Event dihapus dari favorit", Toast.LENGTH_SHORT).show()
                    refreshFavoriteEvents() // Refresh list setelah dihapus
                }
            } catch (e: Exception) {
                Log.e("FavoriteFragment", "Error deleting event", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Gagal menghapus event", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Fungsi untuk merefresh data event favorit
    private fun refreshFavoriteEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            val events = eventDao.getAllFavorites() // Mengambil ulang data dari database
            Log.d("RefreshFavorites", "Fetched events: $events") // Tambahkan log ini
            setupListView(events) // Set ulang adapter dengan data yang baru
        }
    }
}