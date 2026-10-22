package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {
    override fun prepare(url: String) = repository.prepare(url)
    override fun play() = repository.play()
    override fun pause() = repository.pause()
    override fun currentPosition(): Long = repository.currentPosition()
    override fun release() = repository.release()

    override fun setOnPreparedListener(listener: () -> Unit) =
        repository.setOnPreparedListener(listener)

    override fun setOnCompletionListener(listener: () -> Unit) =
        repository.setOnCompletionListener(listener)
}