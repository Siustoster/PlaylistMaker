package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.player.PlayerInteractor
import java.text.SimpleDateFormat
import java.util.Locale

enum class PlayerStatus { DEFAULT, PREPARED, PLAYING, PAUSED }

data class PlayerState(
    val status: PlayerStatus,
    val position: String
) {
    val isPlayButtonEnabled: Boolean get() = status != PlayerStatus.DEFAULT
    val isPlaying: Boolean get() = status == PlayerStatus.PLAYING
}

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {

    companion object {
        private const val TIME_REFRESH_DELAY = 500L
        private const val DEFAULT_POSITION = "00:00"

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(Creator.providePlayerInteractor())
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    private val mutableState = MutableLiveData(PlayerState(PlayerStatus.DEFAULT, DEFAULT_POSITION))
    val state: LiveData<PlayerState> = mutableState

    private val timeRunnable = object : Runnable {
        override fun run() {
            val current = mutableState.value ?: return
            if (current.status == PlayerStatus.PLAYING) {
                mutableState.value = current.copy(
                    position = dateFormat.format(playerInteractor.currentPosition())
                )
                handler.postDelayed(this, TIME_REFRESH_DELAY)
            }
        }
    }

    init {
        playerInteractor.setOnPreparedListener {
            mutableState.value = mutableState.value?.copy(status = PlayerStatus.PREPARED)
        }
        playerInteractor.setOnCompletionListener {
            handler.removeCallbacks(timeRunnable)
            mutableState.value = mutableState.value?.copy(
                status = PlayerStatus.PREPARED,
                position = DEFAULT_POSITION
            )
        }
    }

    fun prepare(url: String) {
        if (mutableState.value?.status != PlayerStatus.DEFAULT) return
        runCatching { playerInteractor.prepare(url) }
            .onFailure {
                mutableState.value = mutableState.value?.copy(status = PlayerStatus.DEFAULT)
            }
    }

    fun togglePlayback() {
        when (mutableState.value?.status) {
            PlayerStatus.PLAYING -> {
                playerInteractor.pause()
                mutableState.value = mutableState.value?.copy(status = PlayerStatus.PAUSED)
            }

            PlayerStatus.PREPARED, PlayerStatus.PAUSED -> {
                playerInteractor.play()
                mutableState.value = mutableState.value?.copy(status = PlayerStatus.PLAYING)
                handler.postDelayed(timeRunnable, TIME_REFRESH_DELAY)
            }

            else -> Unit
        }
    }

    fun pause() {
        if (mutableState.value?.status == PlayerStatus.PLAYING) {
            playerInteractor.pause()
            mutableState.value = mutableState.value?.copy(status = PlayerStatus.PAUSED)
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(timeRunnable)
        playerInteractor.release()
    }
}
