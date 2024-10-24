package com.dicoding.eventdicoding.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.eventdicoding.R
import com.dicoding.eventdicoding.databinding.ActivityMainBinding
import com.dicoding.eventdicoding.fragments.FinishFragment
import com.dicoding.eventdicoding.fragments.UpcomingFragment
import com.dicoding.eventdicoding.fragments.HomeFragment
import com.dicoding.eventdicoding.fragments.SettingsFragment
import com.dicoding.eventdicoding.ui.favorite.FavoriteFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationBar: NavigationBarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load preferences
        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)

        // Apply saved theme
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }


        navigationBar = binding.bottomNavigation
        navBarClick()

        // Memuat HomeFragment saat aplikasi pertama kali dibuka
        if (isNetworkAvailable()) {
            loadFragment(HomeFragment())
        } else {
            showError(getString(R.string.no_inet))
        }
    }

    private fun navBarClick() {
        navigationBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (isNetworkAvailable()) {
                        loadFragment(HomeFragment())
                    } else {
                        showError(getString(R.string.no_inet))
                    }
                    true
                }
                R.id.navigation_upcoming -> {
                    if (isNetworkAvailable()) {
                        loadFragment(UpcomingFragment())
                    } else {
                        showError(getString(R.string.no_inet))
                    }
                    true
                }
                R.id.navigation_finished -> {
                    if (isNetworkAvailable()) {
                        loadFragment(FinishFragment())
                    } else {
                        showError(getString(R.string.no_inet))
                    }
                    true
                }
                R.id.navigation_favorite -> { // Tambahkan logika untuk fragment favorit
                    if (isNetworkAvailable()) {
                        loadFragment(FavoriteFragment()) // Pastikan FavoriteFragment sudah dibuat
                    } else {
                        showError(getString(R.string.no_inet))
                    }
                    true
                }
                R.id.navigation_settings -> { // Tambahkan logika untuk fragment pengaturan
                    if (isNetworkAvailable()) {
                        loadFragment(SettingsFragment()) // Pastikan SettingsFragment sudah dibuat
                    } else {
                        showError(getString(R.string.no_inet))
                    }
                    true
                }
                else -> false
            }
        }
    }

    // Fungsi untuk memuat fragment
    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.frameContainer.id, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    // Fungsi untuk memeriksa koneksi internet
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    // Fungsi untuk menampilkan pesan error
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}