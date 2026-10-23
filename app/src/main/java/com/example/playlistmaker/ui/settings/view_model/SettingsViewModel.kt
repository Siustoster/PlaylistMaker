package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val themeLiveDataMutable =
        MutableLiveData(settingsInteractor.getThemeSettings().darkTheme)
    val themeLiveData: LiveData<Boolean> = themeLiveDataMutable

    fun switchTheme(isDark: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(isDark))
        themeLiveDataMutable.value = isDark
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }
}
