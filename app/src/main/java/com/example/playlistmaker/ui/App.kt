package com.example.playlistmaker.ui

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.model.ThemeSettings


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.init(this)

        val settingsInteractor = Creator.provideSettingsInteractor()
        if (settingsInteractor.getThemeSettings().darkTheme) {
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val initialSwitchState = when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
            settingsInteractor.updateThemeSetting(ThemeSettings(initialSwitchState))
        }
        switchTheme(settingsInteractor.getThemeSettings().darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES

            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }

}