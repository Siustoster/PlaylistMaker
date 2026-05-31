package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageButton>(R.id.pref_back_button)
        val appShareButton = findViewById<LinearLayout>(R.id.appShareButton)
        val supportButton = findViewById<LinearLayout>(R.id.supportButton)
        val licenceAgreementButton = findViewById<LinearLayout>(R.id.licenceAgreementButton)
        backButton.setOnClickListener {
            finish()
        }
        appShareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.course_link)
            )
            startActivity(shareIntent)
        }
        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:" + getString(R.string.developer_email))
            supportIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.support_mail_text)
            )
            supportIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.support_mail_subject)
            )
            startActivity(supportIntent)
        }
        licenceAgreementButton.setOnClickListener {
            val licenceIntent = Intent(Intent.ACTION_VIEW)
            licenceIntent.data = Uri.parse(getString(R.string.license_agreement_link))
            startActivity(licenceIntent)
        }
    }
}