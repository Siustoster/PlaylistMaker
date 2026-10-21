package com.example.playlistmaker.domain.search.Impl

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.HistoryInteractor
import com.example.playlistmaker.domain.search.HistoryRepository

class HistoryInteractorImpl(private val repository: HistoryRepository) : HistoryInteractor {
    override fun getHistory(): List<Track> = repository.getHistory()

    override fun clearHistory() = repository.clearHistory()

    override fun saveTrackToHistory(track: Track) {
        val currentHistory = repository.getHistory().toMutableList()
        currentHistory.removeAll { it.trackId == track.trackId }
        currentHistory.add(0, track)
        repository.saveTracksToHistory(currentHistory.take(10))
    }
}