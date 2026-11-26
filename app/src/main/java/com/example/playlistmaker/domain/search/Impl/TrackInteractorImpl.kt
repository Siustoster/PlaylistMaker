package com.example.playlistmaker.domain.search.Impl

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.MainThread
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

class TrackInteractorImpl(
    private val repository: TrackRepository,
    private val mainTread: MainThread,
) : TrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(term: String): Flow<Result<List<Track>>> {
        return repository.searchTrack(term)
    }
}