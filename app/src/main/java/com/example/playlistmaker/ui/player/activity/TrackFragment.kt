package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentTrackBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.utils.Utilities
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.getValue

class TrackFragment : Fragment() {
    companion object {
        private const val TRACK_NAME = "trackName"
        private const val ARTIST_NAME = "artistName"
        private const val TRACK_TIME = "trackTime"
        private const val ARTWORK_URL = "artworkUrl100"
        private const val COLLECTION_NAME = "collectionName"
        private const val RELEASE_DATE = "releaseDate"
        private const val GENRE_NAME = "primaryGenreName"
        private const val COUNTRY = "country"
        private const val PREVIEW_URL = "previewUrl"
        fun createArgs(
            trackName: String,
            artistName: String,
            trackTime: String,
            artwork: String,
            collectionName: String,
            releaseDate: String,
            primaryGenreName: String,
            country: String,
            previewUrl: String
        ): Bundle = bundleOf(
            TRACK_NAME to trackName, ARTIST_NAME to artistName, TRACK_TIME to trackTime,
            ARTWORK_URL to artwork, COLLECTION_NAME to collectionName, RELEASE_DATE to releaseDate,
            GENRE_NAME to primaryGenreName, COUNTRY to country, PREVIEW_URL to previewUrl
        )

    }

    private lateinit var binding: FragmentTrackBinding
    private val viewModel: PlayerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = Track(
            "0",
            trackName = requireArguments().getString((TRACK_NAME)) ?: "",
            artistName = requireArguments().getString((ARTIST_NAME)) ?: "",
            trackTime = requireArguments().getString((TRACK_TIME)) ?: "",
            artworkUrl100 = requireArguments().getString((ARTWORK_URL)) ?: "",
            collectionName = requireArguments().getString((COLLECTION_NAME)) ?: "",
            releaseDate = requireArguments().getString((RELEASE_DATE)) ?: "",
            primaryGenreName = requireArguments().getString((GENRE_NAME)) ?: "",
            country = requireArguments().getString((COUNTRY)) ?: "",
            previewUrl = requireArguments().getString((PREVIEW_URL)) ?: ""
        )
        bindTrack(track)


        observeViewModel()

        binding.trackBackArrow.setOnClickListener {
            findNavController().navigateUp()
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
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.playButton.isEnabled = state.isPlayButtonEnabled
            binding.playButton.setImageResource(
                if (state.isPlaying) R.drawable.ic_pause_button_100
                else R.drawable.ic_play_button_100
            )
            binding.currentTrackPlayTime.text = state.position
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}