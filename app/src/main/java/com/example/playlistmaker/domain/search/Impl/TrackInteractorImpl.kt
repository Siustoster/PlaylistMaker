package com.example.playlistmaker.domain.search.Impl

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository,
                          private val mainHandler: Handler = Handler(Looper.getMainLooper())
) : TrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(term: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            val result = repository.searchTrack(term)
            mainHandler.post { consumer.consume(result) }
        }
    }
}