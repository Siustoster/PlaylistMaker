package com.example.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.MediaView
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button_pm)
        val mediaButton = findViewById<Button>(R.id.media_button_pm)
        val prefButton = findViewById<Button>(R.id.pref_button_pm)

        searchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        mediaButton.setOnClickListener {
            startActivity(Intent(this, MediaView::class.java))
        }
        prefButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}