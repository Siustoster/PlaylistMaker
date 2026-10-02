package com.example.playlistmaker.presentation.ui

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
//import com.example.playlistmaker.presentation.ui.SearchActivity.Companion.PLAYLIST_MAKER_PREFERENCES
import androidx.core.content.edit
import com.example.playlistmaker.Creator

const val DARK_THEME_KEY = "dark_theme_enabled"

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        Creator.init(this)
        val sharedPreferences = Creator.provideSharedPreferencesInteractor()
        var savedTheme = sharedPreferences.getSP(DARK_THEME_KEY, "")
        if (savedTheme.isEmpty()) {
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val initialSwitchState = when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
            savedTheme = initialSwitchState.toString()
            sharedPreferences.setSP(DARK_THEME_KEY, initialSwitchState.toString())
        }
        switchTheme(savedTheme.toBoolean())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES

            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        val sharedPreferences = Creator.provideSharedPreferencesInteractor()
        sharedPreferences.setSP(DARK_THEME_KEY, darkThemeEnabled.toString())
    }
}