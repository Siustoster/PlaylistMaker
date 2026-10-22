package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.search.SharedPreferencesRepository
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings

class SettingsInteractorImpl(private val repository: SharedPreferencesRepository) :
    SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings =
        ThemeSettings(repository.getSP(ThemeSettings.KEY, "false").toBoolean())

    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.setSP(ThemeSettings.KEY, settings.darkTheme.toString())
    }

    override fun hasThemeSettings(): Boolean =
        repository.getSP(ThemeSettings.KEY, "").isNotEmpty()


}