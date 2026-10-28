package com.example.playlistmaker.di

import com.example.playlistmaker.ui.media.view_model.FavouritesViewModel
import com.example.playlistmaker.ui.media.view_model.MediaViewModel
import com.example.playlistmaker.ui.media.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel{
        PlayerViewModel(get())
    }
    viewModel{
        SearchViewModel(get(),get())
    }
    viewModel{
        SettingsViewModel(get(),get())
    }
    viewModel { MediaViewModel() }
    viewModel { PlaylistViewModel() }
    viewModel { FavouritesViewModel() }
}