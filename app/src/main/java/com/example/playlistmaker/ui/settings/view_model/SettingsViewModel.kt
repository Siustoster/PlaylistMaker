package com.example.playlistmaker.ui.settings.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.model.EmailData
import com.example.playlistmaker.ui.App

class SettingsViewModel(
    private val app: Application,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {
    companion object {

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                SettingsViewModel(
                    app,
                    Creator.provideSharingInteractor(),
                    Creator.provideSettingsInteractor()
                )
            }
        }
    }

    private val themeLiveDataMutable =
        MutableLiveData(settingsInteractor.getThemeSettings().darkTheme)
    val themeLiveData: LiveData<Boolean> = themeLiveDataMutable

    fun switchTheme(isDark: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(isDark))
        themeLiveDataMutable.value = isDark
    }

    fun shareApp() {
        sharingInteractor.shareApp(app.getString(R.string.course_link))
    }

    fun openSupport() {
        sharingInteractor.openSupport(
            EmailData(
                email = app.getString(R.string.developer_email),
                subject = app.getString(R.string.support_mail_subject),
                text = app.getString(R.string.support_mail_text)
            )
        )
    }

    fun openTerms() {
        sharingInteractor.openTerms(app.getString(R.string.license_agreement_link))
    }
}