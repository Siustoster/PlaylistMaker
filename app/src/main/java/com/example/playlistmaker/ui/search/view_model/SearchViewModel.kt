package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.HistoryInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var searchDebounceJob: Job? = null
    private var clickDebounceJob: Job? = null

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

        searchDebounceJob?.cancel()

        if (searchString.isBlank()) {
            mutableState.value = SearchState.Idle
        } else {
            searchDebounceJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                performSearch()
            }
        }
    }

    fun onSearchRetry() {
        searchDebounceJob?.cancel()
        performSearch()
    }

    private fun performSearch() {
        val query = lastQuery
        if (query.isBlank()) return

        mutableState.value = SearchState.Loading
        if (query != lastQuery) return
        viewModelScope.launch {
            trackInteractor.searchTrack(query).collect {
                result ->
                mutableState.value = result.fold(
                    onSuccess = {tracks ->
                        if (tracks.isEmpty()) SearchState.Empty else SearchState.Content(tracks)},
                    onFailure = { SearchState.Error }

                )
            }
        }
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
        clickDebounceJob = viewModelScope.launch {
            delay(CLICK_DEBOUNCE_DELAY)
            isClickAllowed = true
        }

        historyInteractor.saveTrackToHistory(track)
        mutableHistory.value = historyInteractor.getHistory()
        mutableOpenPlayer.value = track
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        mutableHistory.value = emptyList()
    }

}