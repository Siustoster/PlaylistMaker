package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityTrackBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.utils.Utilities
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrackBinding
    private val viewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(
            binding.root
        )
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val track = Track(
            "0",
            trackName = intent.getStringExtra("track_name") ?: "",
            artistName = intent.getStringExtra("artist_name") ?: "",
            trackTime = intent.getStringExtra("duration") ?: "",
            artworkUrl100 = intent.getStringExtra("albumCover") ?: "",
            collectionName = intent.getStringExtra("collectionName") ?: "",
            releaseDate = intent.getStringExtra("year") ?: "",
            primaryGenreName = intent.getStringExtra("genreName") ?: "",
            country = intent.getStringExtra("country") ?: "",
            previewUrl = intent.getStringExtra("previewURL") ?: ""
        )
        bindTrack(track)
        observeViewModel()

        binding.trackBackArrow.setOnClickListener {
            finish()
        }
        binding.playButton.setOnClickListener { viewModel.togglePlayback() }
        track.previewUrl?.takeIf { it.isNotBlank() }?.let(viewModel::prepare)

    }
    private fun bindTrack(track: Track) {
        binding.taTrackName.text = track.trackName
        binding.authorName.text = track.artistName
        binding.trackLength.text = formatDuration(track.trackTime)

        binding.albumGroup.isVisible = !track.collectionName.isNullOrBlank()
        binding.albumName.text = track.collectionName

        binding.countryGroup.isVisible = !track.country.isNullOrBlank()
        binding.country.text = track.country

        binding.genreGroup.isVisible = !track.primaryGenreName.isNullOrBlank()
        binding.genre.text = track.primaryGenreName

        binding.yearGroup.isVisible = !track.releaseDate.isNullOrBlank()
        binding.year.text = formatYear(track.releaseDate)

        Glide.with(binding.albumCover)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .centerInside()
            .transform(RoundedCorners(Utilities.dpToPx(8f, binding.albumCover.context)))
            .placeholder(R.drawable.ic_album_placeholder_312)
            .into(binding.albumCover)
    }
    private fun formatDuration(millis: String?): String {
        val value = millis?.toIntOrNull() ?: return "00:00"
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(value)
    }
    private fun formatYear(date: String?): String =
        runCatching {
            LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME).year.toString()
        }.getOrDefault("")

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
            binding.playButton.isEnabled = state != PlayerViewModel.STATE_DEFAULT
            val icon = if (state == PlayerViewModel.STATE_PLAYING) {
                R.drawable.ic_pause_button_100
            } else {
                R.drawable.ic_play_button_100
            }
            binding.playButton.setImageResource(icon)
        }
        viewModel.position.observe(this) { binding.currentTrackPlayTime.text = it }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}