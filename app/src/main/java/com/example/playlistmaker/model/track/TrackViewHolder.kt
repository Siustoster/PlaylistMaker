package com.example.playlistmaker.model.track

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.utils.Utilities
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(val trackView: View) : RecyclerView.ViewHolder(trackView) {
    private val trackImage: ImageView = trackView.findViewById(R.id.track_image)
    private val trackName: TextView = trackView.findViewById(R.id.track_name)
    private val artistName: TextView = trackView.findViewById(R.id.track_artist_name)
    private val duration: TextView = trackView.findViewById(R.id.track_duration)

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime.toInt())
        Glide.with(trackView)
            .load(track.artworkUrl100)
            .centerInside()
            .transform(RoundedCorners(Utilities.dpToPx(2.0f, trackView.context)))
            .placeholder(R.drawable.ic_track_placeholder)
            .into(trackImage)

    }
}