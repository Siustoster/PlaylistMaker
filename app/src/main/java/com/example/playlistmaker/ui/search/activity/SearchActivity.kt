package com.example.playlistmaker.ui.search.activity

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchViewBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.view_model.SearchState
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.player.activity.TrackActivity
import com.example.playlistmaker.ui.track.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchViewBinding
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initAdapters()
        initListeners()
        observeViewModel()

    }

    private fun initAdapters() {
        adapter = TrackAdapter(emptyList()) { viewModel.onTrackClicked(it) }
        historyAdapter = TrackAdapter(emptyList()) { viewModel.onTrackClicked(it) }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun initListeners() {
        binding.searchBackButton.setOnClickListener { finish() }
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
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
        }

        binding.searchRefreshButton.setOnClickListener { viewModel.onSearchRetry() }
        binding.clearHistoryButton.setOnClickListener { viewModel.clearHistory() }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state -> render(state) }
        viewModel.history.observe(this) { tracks ->
            historyAdapter.update(tracks)
            updateHistoryVisibility()
        }
        viewModel.openPlayer.observe(this) { track -> openPlayer(track) }
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
        val intent = Intent(this, TrackActivity::class.java)

        intent.putExtra("track_name", track.trackName)
        intent.putExtra("artist_name", track.artistName)
        intent.putExtra("duration", track.trackTime)
        intent.putExtra("collectionName", track.collectionName)
        intent.putExtra("country", track.country)
        intent.putExtra("genreName", track.primaryGenreName)
        intent.putExtra("albumCover", track.artworkUrl100)
        intent.putExtra("year", track.releaseDate)
        intent.putExtra("previewURL", track.previewUrl)


        startActivity(intent)
    }
}