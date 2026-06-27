package com.example.playlistmaker.model.track

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R

class TrackViewHolder(val trackView: View) : RecyclerView.ViewHolder(trackView) {
    private val trackImage: ImageView = trackView.findViewById(R.id.track_image)
    private val trackName: TextView = trackView.findViewById(R.id.track_name)
    private val artistName: TextView = trackView.findViewById(R.id.track_artist_name)
    private val duration: TextView = trackView.findViewById(R.id.track_duration)

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text = track.trackTime
        Glide.with(trackView)
            .load(track.artworkUrl100)
            .centerInside()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.ic_media)
            .into(trackImage)

    }
}