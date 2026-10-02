package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SharedPreferencesInteractor
import com.example.playlistmaker.domain.api.SharedPreferencesRepository

class SharedPreferencesInteractorImpl(val repository: SharedPreferencesRepository) :
    SharedPreferencesInteractor {
    override fun getSP(key: String, default: String): String = repository.getSP(key, default)
    override fun setSP(key: String, value: String) = repository.setSP(key, value)
    override fun deleteSP(key: String) = repository.deleteSP(key)
}