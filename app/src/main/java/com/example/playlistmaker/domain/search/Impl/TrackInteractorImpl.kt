package com.example.playlistmaker.domain.search.Impl

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import kotlinx.coroutines.flow.Flow


class TrackInteractorImpl(
    private val repository: TrackRepository
) : TrackInteractor {

    override fun searchTrack(term: String): Flow<Result<List<Track>>> {
        return repository.searchTrack(term)
    }
}