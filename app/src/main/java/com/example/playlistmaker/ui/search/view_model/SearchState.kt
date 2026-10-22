package com.example.playlistmaker.ui.search.view_model

import com.example.playlistmaker.domain.models.Track

sealed interface SearchState {
    data object Idle : SearchState
    data object Loading : SearchState
    data class Content(val tracks: List<Track>) : SearchState
    data object Empty : SearchState
    data object Error : SearchState
}