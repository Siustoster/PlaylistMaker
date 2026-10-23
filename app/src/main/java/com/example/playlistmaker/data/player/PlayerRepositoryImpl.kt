package com.example.playlistmaker.data.player

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.PlayerRepository

class PlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : PlayerRepository {

    private var onPrepared: () -> Unit = {}
    private var onCompletion: () -> Unit = {}




    override fun prepare(url: String) {
        mediaPlayer.apply {
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
        //mediaPlayer = null
    }

    override fun setOnPreparedListener(listener: () -> Unit) {
        onPrepared = listener
        mediaPlayer.setOnPreparedListener { onPrepared() }
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        onCompletion = listener
        mediaPlayer.setOnCompletionListener { onCompletion() }
    }
}