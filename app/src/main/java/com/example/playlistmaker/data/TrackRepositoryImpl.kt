package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackApiRequest
import com.example.playlistmaker.data.dto.TrackApiResponse
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(term: String): List<Track> {
        val response = networkClient.doRequest(TrackApiRequest(term))
        if (response.resultCode == 200) {
            return (response as TrackApiResponse).trackList.map(TrackMapper::toDomain)
        } else return emptyList()
    }
}