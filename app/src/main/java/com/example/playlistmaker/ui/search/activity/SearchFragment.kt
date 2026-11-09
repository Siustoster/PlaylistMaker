package com.example.playlistmaker.ui.search.activity

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentSearchViewBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.view_model.SearchState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.track.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.player.activity.TrackFragment

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchViewBinding
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        initListeners()
        observeViewModel()
    }

    private fun initAdapters() {
        adapter = TrackAdapter(emptyList()) { viewModel.onTrackClicked(it) }
        historyAdapter = TrackAdapter(emptyList()) { viewModel.onTrackClicked(it) }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun initListeners() {
        binding.searchEditText.doAfterTextChanged { text ->
            val searchString = text?.toString().orEmpty()
            viewModel.onSearchStringChanged(searchString)
            binding.clearSearchButton.isVisible = searchString.isNotEmpty()
            updateHistoryVisibility()
        }

        binding.searchEditText.setOnFocusChangeListener { _, _ -> updateHistoryVisibility() }

        binding.clearSearchButton.setOnClickListener {
            binding.searchEditText.text.clear()
            binding.searchEditText.clearFocus()
            val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
        }

        binding.searchRefreshButton.setOnClickListener { viewModel.onSearchRetry() }
        binding.clearHistoryButton.setOnClickListener { viewModel.clearHistory() }
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state -> render(state) }
        viewModel.history.observe(viewLifecycleOwner) { tracks ->
            historyAdapter.update(tracks)
            updateHistoryVisibility()
        }
        viewModel.openPlayer.observe(viewLifecycleOwner) { track -> openPlayer(track) }
    }

    private fun render(state: SearchState) {
        binding.searchProgressBar.isVisible = state is SearchState.Loading
        binding.recyclerView.isVisible = state is SearchState.Content

        binding.searchErrorPlaceholder.isVisible = state is SearchState.Empty
        binding.searchErrorPlaceholderText.isVisible = state is SearchState.Empty

        binding.internetErrorPlaceholder.isVisible = state is SearchState.Error
        binding.internetErrorPlaceholderText.isVisible = state is SearchState.Error
        binding.searchRefreshButton.isVisible = state is SearchState.Error

        if (state is SearchState.Content) adapter.update(state.tracks)
    }

    private fun updateHistoryVisibility() {
        binding.historyLayout.isVisible =
            binding.searchEditText.text.isNullOrEmpty() &&
                    binding.searchEditText.hasFocus() &&
                    viewModel.history.value.orEmpty().isNotEmpty()
    }

    private fun openPlayer(track: Track) {

        findNavController().navigate(
            R.id.action_searchFragment_to_trackFragment2,
            TrackFragment.createArgs(
                trackName = track.trackName,
                artistName = track.artistName,
                trackTime = track.trackTime,
                artwork = track.artworkUrl100.orEmpty(),
                collectionName = track.collectionName.orEmpty(),
                releaseDate = track.releaseDate.orEmpty(),
                primaryGenreName = track.primaryGenreName.orEmpty(),
                country = track.country.orEmpty(),
                previewUrl = track.previewUrl.orEmpty()
            )
        )
    }
}