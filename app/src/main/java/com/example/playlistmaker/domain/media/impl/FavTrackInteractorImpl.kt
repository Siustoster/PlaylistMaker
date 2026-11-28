package com.example.playlistmaker.domain.media.impl

import com.example.playlistmaker.data.db.FavTrackRepository
import com.example.playlistmaker.data.favorites.FavTrackInteractor
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavTrackInteractorImpl(private val favTrackRepository: FavTrackRepository) : FavTrackInteractor {
    override fun getFavTracks(): Flow<List<Track>> {
        return favTrackRepository.getFavTracks()
    }

    override suspend fun addToFavorites(track: Track) {
        favTrackRepository.addToFavorites(track)
    }

    override suspend fun deleteFavTrack(trackId: String) {
        favTrackRepository.deleteFavTrack(trackId)
    }

    override fun getFavTracksIds(): Flow<List<String>> {
        return favTrackRepository.getFavTracksIds()
    }
}