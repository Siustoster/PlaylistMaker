package com.example.playlistmaker.ui

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.uiModule
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin


class App : Application(), KoinComponent {
    private val settingsInteractor: SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@App)
            modules(dataModule, uiModule, domainModule)
        }

        if (!settingsInteractor.hasThemeSettings()) {
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            settingsInteractor.updateThemeSetting(ThemeSettings(currentNightMode == Configuration.UI_MODE_NIGHT_YES))
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