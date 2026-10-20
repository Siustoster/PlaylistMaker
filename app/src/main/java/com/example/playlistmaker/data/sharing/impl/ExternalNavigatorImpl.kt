package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink(lnk: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            lnk
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun openLink(lnk: String) {
        val licenceIntent = Intent(Intent.ACTION_VIEW)
        licenceIntent.data = Uri.parse(lnk)
        licenceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(licenceIntent)
    }

    override fun openEmail(email: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(email.email)
        )
        supportIntent.putExtra(
            Intent.EXTRA_TEXT,
            email.text
        )
        supportIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            email.subject
        )
        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(supportIntent)
    }
}