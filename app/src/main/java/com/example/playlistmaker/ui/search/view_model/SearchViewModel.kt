package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utils.SingleLiveEvent

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    Creator.provideTrackInteractor(),
                    Creator.provideSearchHistoryInteractor()
                )
            }
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { performSearch() }

    private var isClickAllowed = true
    private var lastQuery: String = ""

    private val mutableState = MutableLiveData<SearchState>(SearchState.Idle)
    val state: LiveData<SearchState> = mutableState

    private val mutableHistory = MutableLiveData(historyInteractor.getHistory())
    val history: LiveData<List<Track>> = mutableHistory

    private val mutableOpenPlayer = SingleLiveEvent<Track>()
    val openPlayer: LiveData<Track> = mutableOpenPlayer

    fun onSearchStringChanged(searchString: String) {
        if (searchString == lastQuery) return
        lastQuery = searchString

        handler.removeCallbacks(searchRunnable)
        if (searchString.isBlank()) {
            mutableState.value = SearchState.Idle
        } else {
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    fun onSearchRetry() {
        handler.removeCallbacks(searchRunnable)
        performSearch()
    }

    private fun performSearch() {
        val query = lastQuery
        if (query.isBlank()) return

        mutableState.value = SearchState.Loading
        trackInteractor.searchTrack(query, object : TrackInteractor.TrackConsumer {
            override fun consume(foundTracks: Result<List<Track>>) {
                if (query != lastQuery) return

                mutableState.value = foundTracks.fold(
                    onSuccess = { tracks ->
                        if (tracks.isEmpty()) SearchState.Empty else SearchState.Content(tracks)
                    },
                    onFailure = { SearchState.Error }
                )
            }
        })
    }

    fun onTrackClicked(track: Track) {
        if (!isClickAllowed) return
        isClickAllowed = false
        handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)

        historyInteractor.saveTrackToHistory(track)
        mutableHistory.value = historyInteractor.getHistory()
        mutableOpenPlayer.value = track
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        mutableHistory.value = emptyList()
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacks(searchRunnable)
    }


}