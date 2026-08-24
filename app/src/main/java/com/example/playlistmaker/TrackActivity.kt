package com.example.playlistmaker


import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private lateinit var play: ImageButton
    private lateinit var trackPlayTime: TextView
    val handler = Handler(Looper.getMainLooper())
    private val timeRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                trackPlayTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, TIME_REFRESH_DELAY)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_track)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.track_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val inTrackName = intent.getStringExtra("track_name")
        val inArtistName = intent.getStringExtra("artist_name")
        val inDuration = intent.getStringExtra("duration")
        val inAlbumName = intent.getStringExtra("collectionName") ?: ""
        val inCountry = intent.getStringExtra("country") ?: ""
        val inGenre = intent.getStringExtra("genreName") ?: ""
        val inAlbumCover = intent.getStringExtra("albumCover")
        val inYear = intent.getStringExtra("year") ?: ""
        val inPreviewUrl = intent.getStringExtra("previewURL") ?: ""
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
        play = findViewById(R.id.playButton)
        trackPlayTime = findViewById(R.id.currentTrackPlayTime)

        backButton.setOnClickListener {
            finish()
        }


        trackName.text = inTrackName
        artistName.text = inArtistName
        if (inAlbumName.isNotBlank()) {
            albumName.text = inAlbumName
            albumGroup.visibility = View.VISIBLE
        } else albumGroup.visibility = View.GONE
        if (inCountry.isNotBlank()) {
            country.text = inCountry
            countryGroup.visibility = View.VISIBLE
        } else countryGroup.visibility = View.GONE
        if (inGenre.isNotBlank()) {
            genre.text = inGenre
            genreGroup.visibility = View.VISIBLE
        } else genreGroup.visibility = View.GONE
        if (inYear.isNotBlank()) {
            year.text = LocalDateTime.parse(inYear, DateTimeFormatter.ISO_DATE_TIME).year.toString()
            yearGroup.visibility = View.VISIBLE
        } else yearGroup.visibility = View.GONE
        length.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(inDuration?.toInt())
        val fixedCoverLink = inAlbumCover?.replaceAfterLast('/', "512x512bb.jpg")
        Glide.with(albumCover)
            .load(fixedCoverLink)
            .centerInside()
            .transform(RoundedCorners(Utilities.dpToPx(8.0f, albumCover.context)))
            .placeholder(R.drawable.ic_album_placeholder_312)
            .into(albumCover)

        if (inPreviewUrl.isNotBlank())
            preparePlayer(inPreviewUrl)

        play.setOnClickListener {
            playbackControl()
            if (playerState == STATE_PLAYING) {
                play.setImageResource(R.drawable.ic_pause_button_100)
                handler.postDelayed(timeRunnable, TIME_REFRESH_DELAY)
            } else {
                play.setImageResource(R.drawable.ic_play_button_100)
                handler.removeCallbacks(timeRunnable)
            }
        }
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            handler.removeCallbacks(timeRunnable)
            trackPlayTime.text = "00:00"
            play.setImageResource(R.drawable.ic_play_button_100)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeRunnable)
        mediaPlayer.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_REFRESH_DELAY = 500L
    }
}