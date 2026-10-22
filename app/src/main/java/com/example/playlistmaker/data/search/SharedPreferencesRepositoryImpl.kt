package com.example.playlistmaker.data.search

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.domain.search.SharedPreferencesRepository

class SharedPreferencesRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SharedPreferencesRepository {
    override fun getSP(key: String, default: String): String {
        return sharedPreferences.getString(key, default) ?: default
    }

    override fun setSP(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    override fun deleteSP(key: String) {
        sharedPreferences.edit { remove(key) }
    }
}
