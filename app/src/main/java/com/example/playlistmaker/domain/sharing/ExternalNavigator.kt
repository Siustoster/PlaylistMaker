package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(lnk: String)
    fun openLink(lnk: String)
    fun openEmail(email: EmailData)
}