package com.example.playlistmaker.ui.media.view_model

import com.example.playlistmaker.domain.models.Track

sealed interface FavState {
    data class Content(val tracks: List<Track>) : FavState
    data object Empty : FavState
}