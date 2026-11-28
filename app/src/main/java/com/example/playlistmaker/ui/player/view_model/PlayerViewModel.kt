package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.favorites.FavTrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

enum class PlayerStatus { DEFAULT, PREPARED, PLAYING, PAUSED }

data class PlayerState(
    val status: PlayerStatus,
    val position: String,
    val isFavourite: Boolean
) {
    val isPlayButtonEnabled: Boolean get() = status != PlayerStatus.DEFAULT
    val isPlaying: Boolean get() = status == PlayerStatus.PLAYING
}

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favTrackInteractor: FavTrackInteractor
) : ViewModel() {

    companion object {
        private const val TIME_REFRESH_DELAY = 300L
        private const val DEFAULT_POSITION = "00:00"
        private const val FAV_CLICK_DEBOUNCE = 1000L

    }
    private var isClickAllowed = true
    private var positionRefreshJob: Job? = null
    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    private val mutableState =
        MutableLiveData(PlayerState(PlayerStatus.DEFAULT, DEFAULT_POSITION, false))
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

    fun prepare(track: Track) {
        if (mutableState.value?.status != PlayerStatus.DEFAULT) return
        if (!track.previewUrl.isNullOrBlank())
            runCatching { playerInteractor.prepare(track.previewUrl) }
                .onFailure {
                    mutableState.value = mutableState.value?.copy(status = PlayerStatus.DEFAULT)
                }
        viewModelScope.launch {
            val favoriteIds = favTrackInteractor.getFavTracksIds().first()
            val isFavorite = track.trackId in favoriteIds

            track.isFavorite = isFavorite
            mutableState.value = mutableState.value?.copy(
                isFavourite = isFavorite
            )
        }
    }

    fun togglePlayback() {
        when (mutableState.value?.status) {
            PlayerStatus.PLAYING -> {
                positionRefreshJob?.cancel()
                playerInteractor.pause()
                mutableState.value = mutableState.value?.copy(status = PlayerStatus.PAUSED)
            }

            PlayerStatus.PREPARED, PlayerStatus.PAUSED -> {
                playerInteractor.play()
                mutableState.value = mutableState.value?.copy(status = PlayerStatus.PLAYING)
                positionRefreshJob?.cancel()
                positionRefreshJob = viewModelScope.launch {
                    while (mutableState.value?.status == PlayerStatus.PLAYING) {
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

    fun onFavClicked(track: Track) {
        if (!isClickAllowed) return

        isClickAllowed = false
        val newValue = !track.isFavorite

        viewModelScope.launch {
            if (newValue) {
                favTrackInteractor.addToFavorites(track)
            } else {
                favTrackInteractor.deleteFavTrack(track.trackId)
            }

            track.isFavorite = newValue
            mutableState.value = mutableState.value?.copy(isFavourite = newValue)

            delay(FAV_CLICK_DEBOUNCE)
            isClickAllowed = true
        }
    }

    fun pause() {
        if (mutableState.value?.status == PlayerStatus.PLAYING) {
            positionRefreshJob?.cancel()
            playerInteractor.pause()
            mutableState.value = mutableState.value?.copy(status = PlayerStatus.PAUSED)
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
    }
}
