package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.course_link)
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun openTerms() {
        val licenceIntent = Intent(Intent.ACTION_VIEW)
        licenceIntent.data = Uri.parse(context.getString(R.string.license_agreement_link))
        licenceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(licenceIntent)
    }

    override fun openSupport() {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(context.getString(R.string.developer_email))
        )
        supportIntent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.support_mail_text)
        )
        supportIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            context.getString(R.string.support_mail_subject)
        )
        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(supportIntent)
    }
}
