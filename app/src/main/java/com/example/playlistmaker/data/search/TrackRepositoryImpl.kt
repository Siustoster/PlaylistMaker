package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.TrackMapper
import com.example.playlistmaker.data.dto.TrackApiRequest
import com.example.playlistmaker.data.dto.TrackApiResponse
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(term: String): Flow<Result<List<Track>>> = flow {

        val response = networkClient.doRequest(TrackApiRequest(term))
        if (response.resultCode == 200) {
            with(response as TrackApiResponse) {
                val data = response.trackList.map(TrackMapper::toDomain)
                emit(Result.success(data))
            }
        } else emit(Result.failure(Throwable("Ошибка при запросе")))

    }

}