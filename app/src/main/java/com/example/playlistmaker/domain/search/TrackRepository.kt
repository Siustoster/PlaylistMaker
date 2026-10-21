package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
    fun searchTrack(term: String): Result<List<Track>>
}