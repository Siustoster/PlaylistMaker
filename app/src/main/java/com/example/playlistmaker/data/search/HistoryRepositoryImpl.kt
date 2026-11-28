package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.TrackMapper
import com.example.playlistmaker.data.favorites.FavTrackInteractor
import com.example.playlistmaker.domain.search.HistoryRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.first

class HistoryRepositoryImpl(
    private val searchHistory: SearchHistoryImpl,
    private val favTrackInteractor: FavTrackInteractor
) : HistoryRepository {
    override suspend fun getHistory(): List<Track> {
        val history = searchHistory.getHistory().map { TrackMapper.toDomain(it) }
        val favIds = favTrackInteractor.getFavTracksIds().first()
        history.forEach { track -> track.isFavorite = track.trackId in favIds }
        return history
    }


    override fun clearHistory() =
        searchHistory.clearHistory()


    override fun saveTracksToHistory(tracks: List<Track>) =
        searchHistory.saveTracksToHistory(tracks.map { TrackMapper.toData(it) })

}