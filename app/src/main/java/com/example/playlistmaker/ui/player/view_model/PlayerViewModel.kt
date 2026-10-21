package com.example.playlistmaker.ui.player.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel : ViewModel() {

    companion object {
         const val STATE_DEFAULT = 0
         const val STATE_PREPARED = 1
         const val STATE_PLAYING = 2
         const val STATE_PAUSED = 3
        private const val TIME_REFRESH_DELAY = 500L
        private const val DEFAULT_POSITION = "00:00"
    }

    private val mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    private val mutableState = MutableLiveData(STATE_DEFAULT)
    val state: LiveData<Int> = mutableState

    private val mutablePosition = MutableLiveData(DEFAULT_POSITION)
    val position: LiveData<String> = mutablePosition

    fun prepare(url: String) {
        if (mutableState.value != STATE_DEFAULT) return
        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener { mutableState.value = STATE_PREPARED }
            mediaPlayer.setOnCompletionListener {
                handler.removeCallbacks(timeRunnable)
                mutablePosition.value = DEFAULT_POSITION
                mutableState.value = STATE_PREPARED
            }
        } catch (e: Exception) {
            mutableState.value = STATE_DEFAULT
        }
    }

    fun togglePlayback() {
        when (mutableState.value) {
            STATE_PLAYING -> {
                mediaPlayer.pause()
                mutableState.value = STATE_PAUSED
            }

            STATE_PREPARED, STATE_PAUSED -> {
                mediaPlayer.start()
                mutableState.value = STATE_PLAYING
                handler.postDelayed(timeRunnable, TIME_REFRESH_DELAY)
            }

            else -> Unit
        }
    }

    fun pause() {
        if (mutableState.value == STATE_PLAYING) {
            mediaPlayer.pause()
            mutableState.value = STATE_PAUSED
        }
    }

    private val timeRunnable = object : Runnable {
        override fun run() {
            if (mutableState.value == STATE_PLAYING) {
                mutablePosition.value = dateFormat.format(mediaPlayer.currentPosition)
                handler.postDelayed(this, TIME_REFRESH_DELAY)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(timeRunnable)
        mediaPlayer.release()
    }
}