package com.example.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

data class TrackApiResponse(
    val resultCount: Int,
    @SerializedName("results") val trackList: List<TrackDto>
) : Response()