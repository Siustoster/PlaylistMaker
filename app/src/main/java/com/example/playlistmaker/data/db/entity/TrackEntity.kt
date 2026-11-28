package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_fav")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)
