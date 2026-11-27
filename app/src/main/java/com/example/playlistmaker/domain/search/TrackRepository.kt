package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrack(term: String): Flow<Result<List<Track>>>
}