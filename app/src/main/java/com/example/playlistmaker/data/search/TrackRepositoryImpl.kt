package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.TrackMapper
import com.example.playlistmaker.data.dto.TrackApiRequest
import com.example.playlistmaker.data.dto.TrackApiResponse
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(term: String): Result<List<Track>> {
        try {
            val response = networkClient.doRequest(TrackApiRequest(term))

            if (response.resultCode == 200) {
                return Result.success((response as TrackApiResponse).trackList.map(TrackMapper::toDomain))
            } else return Result.success(emptyList())
        } catch (e: Throwable) {
            return Result.failure(e)
        }
    }

}