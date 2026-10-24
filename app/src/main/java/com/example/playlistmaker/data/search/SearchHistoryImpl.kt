package com.example.playlistmaker.data.search

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.data.dto.TrackDto
import com.google.gson.Gson

const val TRACK_LIST_KEY = "saved_track_list"

class SearchHistoryImpl(val sharedPreferences: SharedPreferences, val gson: Gson) {

    fun getHistory(): List<TrackDto> {
        val json = sharedPreferences.getString(TRACK_LIST_KEY, "[]").orEmpty()
        return gson.fromJson(json, Array<TrackDto>::class.java).toList()
    }

    fun saveTracksToHistory(trackList: List<TrackDto>) {
        sharedPreferences.edit { putString(TRACK_LIST_KEY, gson.toJson(trackList)) }
    }

    fun clearHistory() {
        sharedPreferences.edit { remove(TRACK_LIST_KEY) }
    }

}
