package com.example.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val initialSwitchState = when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
        themeSwitch.isChecked = initialSwitchState
        themeSwitch.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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