package com.example.playlistmaker.data.favorites

import com.example.playlistmaker.data.TrackMapper
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.FavTrackRepository
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavTrackRepositoryImpl(private val appDatabase: AppDatabase) : FavTrackRepository {
    override fun getFavTracks(): Flow<List<Track>> =
        appDatabase.trackDao().getFavTracks().map(::convertFromEntity)

    override suspend fun addToFavorites(track: Track) =
        appDatabase.trackDao().insertTrack(TrackMapper.toDao(track))

    override suspend fun deleteFavTrack(trackId: String) =
        appDatabase.trackDao().deleteTrack(trackId)

    override fun getFavTracksIds(): Flow<List<String>> = flow {
        val ids = appDatabase.trackDao().getFavTracksIds()
        emit(ids)
    }

    private fun convertFromEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> TrackMapper.toDomain(track) }
    }
}