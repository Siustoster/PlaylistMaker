package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Track

interface HistoryInteractor {
    suspend fun getHistory(): List<Track>
    fun clearHistory()
    suspend fun saveTrackToHistory(track: Track)
}