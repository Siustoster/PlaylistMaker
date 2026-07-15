package com.example.playlistmaker.model.track

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.model.Observer

class TrackAdapter(
    private var tracks: List<Track>,
    private val searchHistory: SearchHistory
) : RecyclerView.Adapter<TrackViewHolder>(), Observer {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        holder.bind(tracks[position])
        holder.trackView.setOnClickListener {
            searchHistory.saveTrackToHistory(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun updateHistory() {
        notifyDataSetChanged()
    }

}