package com.example.playlistmaker.di

import com.example.playlistmaker.data.MainThreadImpl
import com.example.playlistmaker.data.search.HistoryRepositoryImpl
import com.example.playlistmaker.data.search.SearchHistoryImpl
import com.example.playlistmaker.domain.search.MainThread
import com.example.playlistmaker.data.search.SharedPreferencesRepositoryImpl
import com.example.playlistmaker.data.search.TrackRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.HistoryInteractor
import com.example.playlistmaker.domain.search.HistoryRepository
import com.example.playlistmaker.domain.search.Impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.search.Impl.TrackInteractorImpl
import com.example.playlistmaker.domain.search.SharedPreferencesRepository
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.factory
import org.koin.dsl.module

val domainModule = module {
    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<SharedPreferencesRepository> {
        SharedPreferencesRepositoryImpl(get())
    }
    factory<MainThread> {
        MainThreadImpl()
    }

    factory<HistoryRepository> {
        HistoryRepositoryImpl(SearchHistoryImpl(get(), get()))
    }
    factory<HistoryInteractor> {
        HistoryInteractorImpl(get())
    }
    factory<TrackRepository> {
        TrackRepositoryImpl(get())
    }
    factory<TrackInteractor> {
        TrackInteractorImpl(get(), get())
    }
    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }
}