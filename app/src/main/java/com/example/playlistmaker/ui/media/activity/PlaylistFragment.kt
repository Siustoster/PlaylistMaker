package com.example.playlistmaker.ui.media.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.example.playlistmaker.ui.media.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {
    private val viewModel: PlaylistViewModel by viewModel()
    private lateinit var binding: PlaylistFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}