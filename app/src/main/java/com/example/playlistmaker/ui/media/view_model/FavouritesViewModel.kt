package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.favorites.FavTrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavouritesViewModel(favTrackInteractor: FavTrackInteractor) : ViewModel() {

    private val mutableState = MutableLiveData<FavState>(FavState.Empty)
    val state: LiveData<FavState> = mutableState
    private val mutableOpenPlayer = SingleLiveEvent<Track>()
    val openPlayer: LiveData<Track> = mutableOpenPlayer
    private var clickDebounceJob: Job? = null
    private var isClickAllowed = true

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    init {
        viewModelScope.launch {
            favTrackInteractor.getFavTracks().collect { tracks ->
                mutableState.value = if (tracks.isEmpty()) {
                    FavState.Empty
                } else {
                    FavState.Content(tracks)
                }
            }
        }
    }

    fun onTrackClicked(track: Track) {
        if (!isClickAllowed) return
        isClickAllowed = false
        clickDebounceJob = viewModelScope.launch {
            delay(CLICK_DEBOUNCE_DELAY)
            isClickAllowed = true
        }

        viewModelScope.launch {
            mutableOpenPlayer.value = track
        }
    }
}