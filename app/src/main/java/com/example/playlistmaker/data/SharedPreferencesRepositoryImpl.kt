package com.example.playlistmaker.data

import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.SharedPreferencesRepository
import androidx.core.content.edit

class SharedPreferencesRepositoryImpl : SharedPreferencesRepository {
    private val sharedPreferences = Creator.provideSharedPreferences()
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