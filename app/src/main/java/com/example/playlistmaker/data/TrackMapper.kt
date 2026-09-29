package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.Track

object TrackMapper {
    fun toDomain(dto: TrackDto): Track = Track(
        trackId = dto.trackId,
        trackName = dto.trackName,
        artistName = dto.artistName,
        trackTime = dto.trackTime,
        artworkUrl100 = dto.trackTime,
        collectionName = dto.collectionName,
        releaseDate = dto.releaseDate,
        primaryGenreName = dto.primaryGenreName,
        country = dto.country,
        previewUrl = dto.previewUrl
    )

    fun toDomain(list: List<TrackDto>): List<Track> =
        list.map(::toDomain)
}