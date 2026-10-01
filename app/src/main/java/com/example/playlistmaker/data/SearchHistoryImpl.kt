package com.example.playlistmaker.data

import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.api.SharedPreferencesInteractor
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

const val TRACK_LIST_KEY = "saved_track_list"

class SearchHistoryImpl(val sharedPreferences: SharedPreferencesInteractor, val gson: Gson) {
    fun getHistory(): List<TrackDto> {
        val json = sharedPreferences.getSP(TRACK_LIST_KEY, "[]")
        return gson.fromJson(json, Array<TrackDto>::class.java).toList()
    }

    fun saveTracksToHistory(trackList: List<TrackDto>) {
        sharedPreferences.setSP(TRACK_LIST_KEY, gson.toJson(trackList))
    }

    fun clearHistory() {
        sharedPreferences.deleteSP(TRACK_LIST_KEY)
    }

}