package com.dicoding.eventdicoding.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.eventdicoding.R
import android.widget.Switch

class SettingsFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val switchDarkMode = view.findViewById<Switch>(R.id.switch_dark_mode)

        // Load saved dark mode preference
        switchDarkMode.isChecked = sharedPreferences.getBoolean("DARK_MODE", false)

        // Set listener for switch
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            // Save preference
            sharedPreferences.edit().putBoolean("DARK_MODE", isChecked).apply()
            // Apply dark mode
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        return view
    }
}