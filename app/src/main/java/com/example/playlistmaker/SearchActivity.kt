package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.model.api.TrackApiResponse
import com.example.playlistmaker.model.track.Track
import com.example.playlistmaker.model.track.TrackAdapter
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class SearchActivity : AppCompatActivity() {

    lateinit var adapter: TrackAdapter
    lateinit var historyAdapter: TrackAdapter
    private lateinit var itunesApiService: ItunesInterfaceApi
    private lateinit var editText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var searchPhImage: ImageView
    private lateinit var searchPhText: TextView
    private lateinit var internetErrorPhImage: ImageView
    private lateinit var internetErrorPhText: TextView
    private lateinit var searchRefreshButton: MaterialButton
    private lateinit var clearHistoryButton: MaterialButton
    private lateinit var historyLayout: LinearLayout
    private lateinit var trackList: ArrayList<Track>
    private lateinit var historyList: MutableList<Track>
    private lateinit var historyListener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var progressBar: ProgressBar
    private var editTextString: String = EDIT_TEXT_DEF
    private var isClickAllowed = true
    val handler = Handler(Looper.getMainLooper())
    val searchRunnable = Runnable { performApiSearch() }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT_NAME, editTextString)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextString = savedInstanceState.getString(EDIT_TEXT_NAME, EDIT_TEXT_DEF)
        val editText = findViewById<EditText>(R.id.search_edit_text)
        editText.setText(editTextString)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        trackList = ArrayList()
        val itunesBaseUrl = "https://itunes.apple.com"
        val retrofit = Retrofit.Builder()
            .baseUrl(itunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        itunesApiService = retrofit.create<ItunesInterfaceApi>()
        editText = findViewById(R.id.search_edit_text)
        searchPhImage = findViewById(R.id.search_error_placeholder)
        searchPhText = findViewById(R.id.search_error_placeholder_text)
        recyclerView = findViewById(R.id.recyclerView)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        searchRefreshButton = findViewById(R.id.searchRefreshButton)
        internetErrorPhImage = findViewById(R.id.internet_error_placeholder)
        internetErrorPhText = findViewById(R.id.internet_error_placeholder_text)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        historyLayout = findViewById(R.id.historyLayout)
        progressBar = findViewById(R.id.searchProgressBar)
        val clearButton = findViewById<ImageView>(R.id.clear_search_button)
        val backButton = findViewById<ImageButton>(R.id.search_back_button)

        backButton.setOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            editText.text.clear()
            editText.clearFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                editText.applicationWindowToken,
                0
            ) //что бы скрыть клавиатуру по тз. Просто очистка фокуса не помогала.
            trackList.clear()
            searchPhText.visibility = View.GONE
            searchPhImage.visibility = View.GONE
            internetErrorPhText.visibility = View.GONE
            internetErrorPhImage.visibility = View.GONE
            searchRefreshButton.visibility = View.GONE
            adapter.notifyDataSetChanged()
        }
        editText.addTextChangedListener { apiSearchDebounce() }
        searchRefreshButton.setOnClickListener {
            performApiSearch()
        }

        val sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        historyListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == TRACK_LIST_KEY) {
                    historyList.clear()
                    historyList.addAll(
                        Gson().fromJson(
                            sharedPreferences.getString(TRACK_LIST_KEY, "[]"),
                            Array<Track>::class.java
                        )
                    )
                    historyAdapter.notifyDataSetChanged()
                }
            }
        sharedPreferences.registerOnSharedPreferenceChangeListener(historyListener)
        val searchHistory = SearchHistory(sharedPreferences)

        editText.doAfterTextChanged { s ->
            editTextString = s.toString()
            if (s.isNullOrEmpty()) {
                if (editText.hasFocus() && searchHistory.getHistory().isNotEmpty())
                    historyLayout.visibility = View.VISIBLE
                clearButton.visibility = View.INVISIBLE
            } else {
                clearButton.visibility = View.VISIBLE
                historyLayout.visibility = View.GONE
            }
        }
        clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            historyLayout.visibility = View.GONE
            historyAdapter.notifyDataSetChanged()
        }
        historyList = searchHistory.getHistory().toMutableList()
        recyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyAdapter = TrackAdapter(historyList, searchHistory) { selectedTrack ->
            if (clickDebounce())
                openPlayer(selectedTrack)
        }
        adapter = TrackAdapter(trackList, searchHistory) { selectedTrack ->
            if (clickDebounce())
                openPlayer(selectedTrack)
        }
        recyclerView.adapter = adapter
        historyRecyclerView.adapter = historyAdapter
        editText.setOnFocusChangeListener { view, hasFocus ->
            historyLayout.visibility =
                if (hasFocus && searchHistory.getHistory().isNotEmpty()) View.VISIBLE else View.GONE
        }

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

    fun apiSearchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun performApiSearch() {

        //уходим от дублирования кода
        if (!editText.text.isEmpty()) {
            progressBar.visibility = View.VISIBLE
            itunesApiService.search(editText.text.toString())
                .enqueue(object : Callback<TrackApiResponse> {
                    override fun onResponse(
                        call: Call<TrackApiResponse?>,
                        response: Response<TrackApiResponse?>
                    ) {
                        if (response.code() == 200) {
                            if (response.body()?.resultCount!! > 0) {
                                recyclerView.visibility = View.VISIBLE
                                searchPhText.visibility = View.GONE
                                searchPhImage.visibility = View.GONE
                                internetErrorPhText.visibility = View.GONE
                                internetErrorPhImage.visibility = View.GONE
                                searchRefreshButton.visibility = View.GONE
                                progressBar.visibility = View.GONE
                                trackList.clear()
                                trackList.addAll(response.body()?.trackList!!)
                                adapter.notifyDataSetChanged()
                            } else {
                                searchPhText.visibility = View.VISIBLE
                                searchPhImage.visibility = View.VISIBLE
                                recyclerView.visibility = View.GONE
                                progressBar.visibility = View.GONE
                                internetErrorPhText.visibility = View.GONE
                                internetErrorPhImage.visibility = View.GONE
                                searchRefreshButton.visibility = View.GONE
                            }
                        }
                    }

                    override fun onFailure(call: Call<TrackApiResponse?>, t: Throwable) {
                        recyclerView.visibility = View.GONE
                        searchPhText.visibility = View.GONE
                        searchPhImage.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        internetErrorPhText.visibility = View.VISIBLE
                        internetErrorPhImage.visibility = View.VISIBLE
                        searchRefreshButton.visibility = View.VISIBLE
                    }
                })
        }
    }

    companion object {
        const val EDIT_TEXT_NAME = "SEARCH_EDIT_TEXT"
        const val EDIT_TEXT_DEF = ""
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val SEARCH_DEBOUNCE_DELAY = 2000L

        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
