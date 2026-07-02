package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.model.api.TrackApiResponse
import com.example.playlistmaker.model.track.Track
import com.example.playlistmaker.model.track.TrackAdapter
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SearchActivity : AppCompatActivity() {
    lateinit var adapter: TrackAdapter
    private lateinit var itunesApiService: ItunesInterfaceApi
    private lateinit var editText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchPhImage: ImageView
    private lateinit var searchPhText: TextView
    private lateinit var internetErrorPhImage: ImageView
    private lateinit var internetErrorPhText: TextView
    private lateinit var searchRefreshButton: MaterialButton
    private lateinit var trackList: ArrayList<Track>
    private var editTextString: String = EDIT_TEXT_DEF


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
        searchRefreshButton = findViewById(R.id.searchRefreshButton)
        internetErrorPhImage = findViewById(R.id.internet_error_placeholder)
        internetErrorPhText = findViewById(R.id.internet_error_placeholder_text)
        val clearButton = findViewById<ImageView>(R.id.clear_search_button)
        val backButton = findViewById<ImageButton>(R.id.search_back_button)
        backButton.setOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            editText.text.clear()
            editText.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
        editText.doAfterTextChanged { s ->
            editTextString = s.toString()
            if (s.isNullOrEmpty()) {
                clearButton.visibility = View.INVISIBLE
            } else clearButton.visibility = View.VISIBLE
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performApiSearch()
                true
            }
            false
        }
        searchRefreshButton.setOnClickListener {
            performApiSearch()
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TrackAdapter(trackList)
        recyclerView.adapter = adapter


    }

    fun performApiSearch() {
        //уходим от дублирования кода
        if (!editText.text.isEmpty())
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
                                trackList.clear()
                                trackList.addAll(response.body()?.trackList!!)
                                adapter.notifyDataSetChanged()
                            } else {
                                searchPhText.visibility = View.VISIBLE
                                searchPhImage.visibility = View.VISIBLE
                                recyclerView.visibility = View.GONE
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
                        internetErrorPhText.visibility = View.VISIBLE
                        internetErrorPhImage.visibility = View.VISIBLE
                        searchRefreshButton.visibility = View.VISIBLE
                    }
                })
    }

    companion object {
        const val EDIT_TEXT_NAME = "SEARCH_EDIT_TEXT"
        const val EDIT_TEXT_DEF = ""
    }
}
