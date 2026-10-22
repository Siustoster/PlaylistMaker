package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Track

interface HistoryInteractor {
    fun getHistory() : List<Track>
    fun clearHistory()
    fun saveTrackToHistory(track: Track)
}