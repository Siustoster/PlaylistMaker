package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Track

interface TrackInteractor {
    fun searchTrack(term: String, consumer: TrackConsumer)
    interface TrackConsumer {
        fun consume(foundTracks: Result<List<Track>>)
    }
}