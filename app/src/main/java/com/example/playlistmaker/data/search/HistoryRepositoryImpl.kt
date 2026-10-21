package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.TrackMapper
import com.example.playlistmaker.domain.search.HistoryRepository
import com.example.playlistmaker.domain.models.Track

class HistoryRepositoryImpl(private val searchHistory: SearchHistoryImpl) : HistoryRepository {
    override fun getHistory(): List<Track> =
        searchHistory.getHistory().map { TrackMapper.toDomain(it) }


    override fun clearHistory() =
        searchHistory.clearHistory()


    override fun saveTracksToHistory(tracks: List<Track>) =
        searchHistory.saveTracksToHistory(tracks.map { TrackMapper.toData(it) })

}