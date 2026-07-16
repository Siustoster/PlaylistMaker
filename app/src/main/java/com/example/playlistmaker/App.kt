package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.SearchActivity.Companion.PLAYLIST_MAKER_PREFERENCES

const val DARK_THEME_KEY = "dark_theme_enabled"

class App : Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        var savedTheme = sharedPreferences.getString(DARK_THEME_KEY, "")!!
        if (savedTheme.isEmpty()) {
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val initialSwitchState = when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
            savedTheme = initialSwitchState.toString()
            sharedPreferences.edit()
                .putString(DARK_THEME_KEY, initialSwitchState.toString())
                .apply()
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
        getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE).edit()
            .putString(DARK_THEME_KEY, darkThemeEnabled.toString())
            .apply()
    }
}