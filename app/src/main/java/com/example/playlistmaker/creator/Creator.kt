package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.MainThreadImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.HistoryRepositoryImpl
import com.example.playlistmaker.data.search.SearchHistoryImpl
import com.example.playlistmaker.data.search.SharedPreferencesRepositoryImpl
import com.example.playlistmaker.data.search.TrackRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.HistoryInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.Impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.search.Impl.TrackInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.google.gson.Gson

object Creator {
    private lateinit var applicationContext: Context
    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    private val gson by lazy { Gson() }
    private val sharedPreferences: SharedPreferences by lazy {
        applicationContext.getSharedPreferences(
            "playlist_maker_preferences",
            Context.MODE_PRIVATE
        )
    }

    private val mainThread by lazy { MainThreadImpl() }

    private val searchHistory by lazy {
        SearchHistoryImpl(sharedPreferences, gson)
    }

    private val historyRepository by lazy {
        HistoryRepositoryImpl(searchHistory)
    }

    private val trackRepository by lazy {
        TrackRepositoryImpl(RetrofitNetworkClient())
    }

    private val sharedPreferencesRepository by lazy {
        SharedPreferencesRepositoryImpl(sharedPreferences)
    }

    private val playerRepository by lazy {
        PlayerRepositoryImpl()
    }

    private val trackInteractor by lazy {
        TrackInteractorImpl(trackRepository, mainThread)
    }

    private val historyInteractor by lazy {
        HistoryInteractorImpl(historyRepository)
    }

    private val settingsInteractor by lazy {
        SettingsInteractorImpl(sharedPreferencesRepository)
    }

    private val sharingInteractor by lazy {
        SharingInteractorImpl(ExternalNavigatorImpl(applicationContext))
    }

    private val playerInteractor by lazy {
        PlayerInteractorImpl(playerRepository)
    }

    fun provideSearchHistoryInteractor(): HistoryInteractor = historyInteractor

    fun provideTrackInteractor(): TrackInteractor = trackInteractor

    fun provideSharingInteractor(): SharingInteractor = sharingInteractor

    fun provideSettingsInteractor(): SettingsInteractor = settingsInteractor

    fun providePlayerInteractor(): PlayerInteractor = playerInteractor
}
