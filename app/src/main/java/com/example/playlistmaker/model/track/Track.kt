package com.example.playlistmaker.model.track

import com.google.gson.annotations.SerializedName

data class Track(
    val trackId:String,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: String,
    val artworkUrl100: String
)