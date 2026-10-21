package com.example.playlistmaker.domain.search.Impl

import com.example.playlistmaker.domain.search.MainThread
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(
    private val repository: TrackRepository,
    private val mainTread: MainThread,
) : TrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(term: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            val result = repository.searchTrack(term)
            mainTread.post { consumer.consume(result) }
        }
    }
}