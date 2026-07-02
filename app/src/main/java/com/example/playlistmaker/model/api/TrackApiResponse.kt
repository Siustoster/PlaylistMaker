package com.example.playlistmaker.model.api

import com.example.playlistmaker.model.track.Track
import com.google.gson.annotations.SerializedName

data class TrackApiResponse(
    val resultCount: Int,
    @SerializedName("results") val trackList: List<Track>
)
