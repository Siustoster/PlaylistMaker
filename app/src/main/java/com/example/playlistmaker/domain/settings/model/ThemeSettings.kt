package com.example.playlistmaker.domain.settings.model

data class ThemeSettings(val darkTheme: Boolean) {
    companion object { const val KEY = "dark_theme_enabled" }
}