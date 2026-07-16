package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.SearchActivity.Companion.PLAYLIST_MAKER_PREFERENCES
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageButton>(R.id.pref_back_button)
        val appShareButton = findViewById<LinearLayout>(R.id.appShareButton)
        val supportButton = findViewById<LinearLayout>(R.id.supportButton)
        val licenceAgreementButton = findViewById<LinearLayout>(R.id.licenceAgreementButton)
        val themeSwitch = findViewById<SwitchMaterial>(R.id.theme_switch)

        themeSwitch.isChecked = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
            .getString(DARK_THEME_KEY, "").toBoolean()
        themeSwitch.setOnCheckedChangeListener { switcher, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }

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
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.developer_email))
            )
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