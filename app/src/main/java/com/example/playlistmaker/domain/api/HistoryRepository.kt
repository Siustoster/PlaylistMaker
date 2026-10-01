package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface HistoryRepository {
    fun getHistory(): List<Track>
    fun clearHistory()
    fun saveTracksToHistory(tracks: List<Track>)
}