package com.example.playlistmaker.data.favorites

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavTrackInteractor {
    fun getFavTracks(): Flow<List<Track>>
    suspend fun addToFavorites(track: Track)
    suspend fun deleteFavTrack(trackId: String)
    fun getFavTracksIds(): Flow<List<String>>
}