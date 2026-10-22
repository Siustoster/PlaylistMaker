package com.example.playlistmaker.domain.search

interface SharedPreferencesRepository {
    fun getSP(key: String, default: String): String
    fun setSP(key: String, value: String)
    fun deleteSP(key: String)
}