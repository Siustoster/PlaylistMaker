package com.example.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.App
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel.themeLiveData.observe(this) { isDark ->
            if (binding.themeSwitch.isChecked != isDark)
                binding.themeSwitch.isChecked = isDark
            (application as App).switchTheme(isDark)
        }

        binding.themeSwitch.setOnCheckedChangeListener { switcher, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        binding.prefBackButton.setOnClickListener {
            finish()
        }

        binding.appShareButton.setOnClickListener {
            viewModel.shareApp()
        }

        binding.supportButton.setOnClickListener {
            viewModel.openSupport()
        }

        binding.licenceAgreementButton.setOnClickListener {
            viewModel.openTerms()
        }
    }
}