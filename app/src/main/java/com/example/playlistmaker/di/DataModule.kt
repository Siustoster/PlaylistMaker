package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.network.ItunesInterfaceApi
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ItunesInterfaceApi> {
        Retrofit.Builder().baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ItunesInterfaceApi::class.java)

    }
    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }
    factory { MediaPlayer() }
    factory<PlayerRepository> { PlayerRepositoryImpl(get()) }
    single {
        androidContext().getSharedPreferences(
            "playlist_maker_preferences", Context.MODE_PRIVATE
        )
    }
    factory { Gson() }


}