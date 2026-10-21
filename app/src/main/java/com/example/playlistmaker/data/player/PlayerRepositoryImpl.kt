package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.PlayerRepository

class PlayerRepositoryImpl: PlayerRepository {

    private var onPrepared: () -> Unit = {}
    private var onCompletion: () -> Unit = {}
    private var mediaPlayer: MediaPlayer? = null

    private fun requirePlayer(): MediaPlayer =
        mediaPlayer ?: MediaPlayer().apply {
            setOnPreparedListener { onPrepared() }
            setOnCompletionListener { onCompletion() }
        }.also { mediaPlayer = it }

    override fun prepare(url: String) {
        requirePlayer().apply {
            setDataSource(url)
            prepareAsync()
        }
    }

    override fun play() {
        mediaPlayer?.start()
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun currentPosition(): Long = mediaPlayer?.currentPosition?.toLong() ?: 0L

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun setOnPreparedListener(listener: () -> Unit) {
        onPrepared = listener
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        onCompletion = listener
    }
}