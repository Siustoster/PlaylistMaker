package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)
    @Query("DELETE FROM tracks_fav WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId:String)
    @Query("Select * FROM tracks_fav order by id desc")
    fun getFavTracks(): Flow<List<TrackEntity>>
    @Query("Select trackId from tracks_fav")
    suspend fun  getFavTracksIds():List<String>
}