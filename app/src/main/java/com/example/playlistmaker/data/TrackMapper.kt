package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.Track

object TrackMapper {
    fun toDomain(dto: TrackDto): Track = Track(
        trackId = dto.trackId,
        trackName = dto.trackName,
        artistName = dto.artistName,
        trackTime = dto.trackTime,
        artworkUrl100 = dto.artworkUrl100?:"",
        collectionName = dto.collectionName,
        releaseDate = dto.releaseDate,
        primaryGenreName = dto.primaryGenreName,
        country = dto.country,
        previewUrl = dto.previewUrl?:""
    )

    fun toDomain(list: List<TrackDto>): List<Track> =
        list.map(::toDomain)
    fun toData(track:Track) : TrackDto = TrackDto(
        trackId = track.trackId,
        trackName = track.trackName,
        artistName = track.artistName,
        trackTime = track.trackTime,
        artworkUrl100 = track.artworkUrl100?:"",
        collectionName = track.collectionName,
        releaseDate = track.releaseDate,
        primaryGenreName = track.primaryGenreName,
        country = track.country,
        previewUrl = track.previewUrl?:""
    )
 }