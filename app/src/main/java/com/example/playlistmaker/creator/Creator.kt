package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.HistoryRepositoryImpl
import com.example.playlistmaker.data.SearchHistoryImpl
import com.example.playlistmaker.data.SharedPreferencesRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.SharedPreferencesInteractor
import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.SharedPreferencesInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
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
    private val searchHistory by lazy {
        SearchHistoryImpl(provideSharedPreferencesInteractor(), gson)
    }

    private val historyRepository by lazy {
        HistoryRepositoryImpl(searchHistory)
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getSharedPreferencesRepository(): SharedPreferencesRepository {
        return SharedPreferencesRepositoryImpl()
    }

    fun provideSharedPreferences(): SharedPreferences = sharedPreferences
    fun provideSharedPreferencesInteractor(): SharedPreferencesInteractor =
        SharedPreferencesInteractorImpl(getSharedPreferencesRepository())

    fun provideSearchHistoryInteractor(): HistoryInteractor =
        HistoryInteractorImpl(historyRepository)

    fun provideTrackInteractor(): TrackInteractor =
        TrackInteractorImpl(getTrackRepository())
    fun provideSharingInteractor(): SharingInteractor =
        SharingInteractorImpl(ExternalNavigatorImpl(applicationContext))
    fun provideSettingsInteractor(): SettingsInteractor = SettingsInteractorImpl(getSharedPreferencesRepository())

}