package com.example.playlistmaker.ui.media.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FavouritesFragmentBinding
import com.example.playlistmaker.ui.media.view_model.FavouritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment: Fragment() {
    private val viewModel: FavouritesViewModel by viewModel()
    private lateinit var binding: FavouritesFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FavouritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}