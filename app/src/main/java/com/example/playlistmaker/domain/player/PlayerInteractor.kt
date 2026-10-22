package com.example.playlistmaker.domain.player

interface PlayerInteractor {
    fun prepare(url: String)
    fun play()
    fun pause()
    fun currentPosition(): Long
    fun release()

    fun setOnPreparedListener(listener: () -> Unit)
    fun setOnCompletionListener(listener: () -> Unit)
}