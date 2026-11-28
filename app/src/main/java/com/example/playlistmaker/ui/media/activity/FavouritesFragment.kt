package com.example.playlistmaker.ui.media.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FavouritesFragmentBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.media.view_model.FavState
import com.example.playlistmaker.ui.media.view_model.FavouritesViewModel
import com.example.playlistmaker.ui.player.activity.TrackFragment
import com.example.playlistmaker.ui.search.view_model.SearchState
import com.example.playlistmaker.ui.track.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {
    private val viewModel: FavouritesViewModel by viewModel()
    private lateinit var adapter: TrackAdapter

    companion object {
        fun newInstance() = FavouritesFragment()
    }

    private var _binding: FavouritesFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavouritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state -> render(state) }
        viewModel.openPlayer.observe(viewLifecycleOwner) { track -> openPlayer(track) }
    }

    private fun initAdapters() {
        adapter = TrackAdapter(emptyList()) { viewModel.onTrackClicked(it) }
        binding.favRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favRecyclerView.adapter = adapter
    }

    private fun render(state: FavState) {
        if (state is FavState.Content) {
            adapter.update(state.tracks)
            binding.favRecyclerView.isVisible = true
            binding.favPlaceholderImg.isVisible = false
            binding.favPlaceholderText.isVisible = false
        } else {
            binding.favRecyclerView.isVisible = false
            binding.favPlaceholderImg.isVisible = true
            binding.favPlaceholderText.isVisible = true
        }
    }

    private fun openPlayer(track: Track) {

        findNavController().navigate(
            R.id.action_mediaFragment_to_trackFragment,
            TrackFragment.createArgs(
                track
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}