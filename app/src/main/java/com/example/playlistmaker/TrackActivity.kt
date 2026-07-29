package com.example.playlistmaker


import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.utils.Utilities
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_track)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.track_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sTrackName = intent.getStringExtra("track_name")
        val sArtistName = intent.getStringExtra("artist_name")
        val sDuration = intent.getStringExtra("duration")
        val sAlbumName = intent.getStringExtra("collectionName") ?: ""
        val sCountry = intent.getStringExtra("country") ?: ""
        val sGenre = intent.getStringExtra("genreName") ?: ""
        val sAlbumCover = intent.getStringExtra("albumCover")
        val sYear = intent.getStringExtra("year") ?: ""
        val albumCover = findViewById<ImageView>(R.id.albumCover)
        val trackName = findViewById<TextView>(R.id.ta_trackName)
        val artistName = findViewById<TextView>(R.id.authorName)
        val length = findViewById<TextView>(R.id.trackLength)
        val country = findViewById<TextView>(R.id.country)
        val genre = findViewById<TextView>(R.id.genre)
        val year = findViewById<TextView>(R.id.year)
        val albumName = findViewById<TextView>(R.id.albumName)
        val albumGroup = findViewById<Group>(R.id.albumGroup)
        val countryGroup = findViewById<Group>(R.id.countryGroup)
        val genreGroup = findViewById<Group>(R.id.genreGroup)
        val yearGroup = findViewById<Group>(R.id.yearGroup)
        val backButton = findViewById<ImageButton>(R.id.trackBackArrow)
        backButton.setOnClickListener {
            finish()
        }


        trackName.text = sTrackName
        artistName.text = sArtistName
        if (sAlbumName.isNotBlank()) {
            albumName.text = sAlbumName
            albumGroup.visibility = View.VISIBLE
        } else albumGroup.visibility = View.GONE
        if (sCountry.isNotBlank()) {
            country.text = sCountry
            countryGroup.visibility = View.VISIBLE
        } else countryGroup.visibility = View.GONE
        if (sGenre.isNotBlank()) {
            genre.text = sGenre
            genreGroup.visibility = View.VISIBLE
        } else genreGroup.visibility = View.GONE
        if (sYear.isNotBlank()) {
            year.text = LocalDateTime.parse(sYear, DateTimeFormatter.ISO_DATE_TIME).year.toString()
            yearGroup.visibility = View.VISIBLE
        } else yearGroup.visibility = View.GONE
        length.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(sDuration?.toInt())
        val fixedCoverLink = sAlbumCover?.replaceAfterLast('/', "512x512bb.jpg")
        Glide.with(albumCover)
            .load(fixedCoverLink)
            .centerInside()
            .transform(RoundedCorners(Utilities.dpToPx(8.0f, albumCover.context)))
            .placeholder(R.drawable.ic_album_placeholder_312)
            .into(albumCover)
    }
}