package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.player.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    }
    private var positionRefreshJob: Job? = null
    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    private val mutableState = MutableLiveData(PlayerState(PlayerStatus.DEFAULT, DEFAULT_POSITION))
    val state: LiveData<PlayerState> = mutableState

    init {
        playerInteractor.setOnPreparedListener {
            mutableState.value = mutableState.value?.copy(status = PlayerStatus.PREPARED)
        }
        playerInteractor.setOnCompletionListener {
            positionRefreshJob?.cancel()
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
                positionRefreshJob = viewModelScope.launch {
                    while(mutableState.value?.status == PlayerStatus.PLAYING) {
                        delay(TIME_REFRESH_DELAY)
                        mutableState.value = mutableState.value?.copy(
                            position = dateFormat.format(playerInteractor.currentPosition())
                        )
                    }
                }
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
        playerInteractor.release()
    }
}
