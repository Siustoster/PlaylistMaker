package com.example.playlistmaker.data.db

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavTrackRepository {
    fun getFavTracks(): Flow<List<Track>>
    suspend fun addToFavorites(track: Track)
    suspend fun deleteFavTrack(trackId:String)
    fun getFavTracksIds() : Flow<List<String>>
}