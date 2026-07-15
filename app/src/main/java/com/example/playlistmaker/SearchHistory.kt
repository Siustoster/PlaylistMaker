package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.model.Observable
import com.example.playlistmaker.model.Observer
import com.example.playlistmaker.model.track.Track
import com.google.gson.Gson

const val TRACK_LIST_KEY = "saved_track_list"

class SearchHistory(val sharedPreferences: SharedPreferences) : Observable {
    private val subscribers = mutableListOf<Observer>()
    fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(TRACK_LIST_KEY, "[]")
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }

    fun saveTrackToHistory(track: Track) {
        val searchHistory: MutableList<Track> = getHistory().toMutableList()
        if (!searchHistory.contains(track)) {
            searchHistory.add(0, track)
            if (searchHistory.size > 10)
                searchHistory.removeAt(10)
        } else {
            searchHistory.removeIf { track.trackId == it.trackId }
            searchHistory.add(0, track)
        }
        val json = Gson().toJson(searchHistory)
        sharedPreferences.edit()
            .putString(TRACK_LIST_KEY, json)
            .apply()
    }

    fun clearHistory() {
        sharedPreferences.edit().remove(TRACK_LIST_KEY).apply()
    }

    override fun add(observer: Observer) {
        subscribers.add(observer)
    }

    override fun remove(observer: Observer) {
        subscribers.remove(observer)
    }

    override fun notifyObservers() {
        subscribers.forEach {
            it.updateHistory()
        }
    }

}