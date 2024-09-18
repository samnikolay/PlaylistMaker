package com.samnic.playlistmaker

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApi = retrofit.create(ITunesApi::class.java)

    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(tracks)

    private lateinit var searchField: EditText
    private lateinit var backButton: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var trackListView: RecyclerView
    private lateinit var emptySearch: LinearLayout
    private lateinit var errorSearch: LinearLayout
    private lateinit var refreshButton: Button


    private var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchField = findViewById(R.id.searchField)
        backButton = findViewById(R.id.arrowBack)
        clearButton = findViewById(R.id.clearIcon)
        trackListView = findViewById(R.id.trackList)
        emptySearch = findViewById(R.id.emptySearch)
        errorSearch = findViewById(R.id.errorSearch)
        refreshButton = findViewById(R.id.refreshButton)

        trackListView.adapter = trackAdapter

        backButton.setOnClickListener {
            finish()
        }

        refreshButton.setOnClickListener {
            Log.e(TAG, "S text: ${searchField.text}")
            resetVisability()
            performSearch()
        }

        clearButton.setOnClickListener {
            searchField.setText("")
            resetVisability()
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }

        searchField.setOnEditorActionListener { _, actionId, _ ->
            resetVisability()
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch()
                true
            }
            false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Тут мне нечего делать пока что
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButtonVisibility(s, clearButton)
                searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // И тут мне нечего делать пока что
            }
        }
        searchField.addTextChangedListener(simpleTextWatcher)
    }

    private fun performSearch() {
        if (searchField.text.isNotEmpty()) {
            iTunesApi.searchTrack(searchField.text.toString(), SEARCH_TACK_LIMIT).enqueue(object :
                Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    Log.i(TAG, "onResponse: ${response.body()}")
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            showStub(EMPTY_SEARCH_STUB)
                        }
                    } else {
                        showStub(ERROR_SEARCH_STUB)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: $t")
                    showStub(ERROR_SEARCH_STUB)
                }

            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchField.setText(savedInstanceState.getString(EXTRA_SEARCH_TEXT, ""))
    }

    private fun clearButtonVisibility(s: CharSequence?, clearButton: ImageView) {
        clearButton.isVisible = !s.isNullOrEmpty()
    }

    private fun showStub(type: String) {
        trackListView.isVisible = false
        when (type) {
            EMPTY_SEARCH_STUB -> {
                emptySearch.isVisible = true
            }

            ERROR_SEARCH_STUB -> {
                errorSearch.isVisible = true
            }
        }
    }

    private fun resetVisability() {
        emptySearch.isVisible = false
        errorSearch.isVisible = false
        trackListView.isVisible = true
    }

    companion object {
        const val EXTRA_SEARCH_TEXT = "searchText"
        const val BASE_URL = "https://itunes.apple.com"
        const val SEARCH_TACK_LIMIT = 25
        const val EMPTY_SEARCH_STUB = "empty"
        const val ERROR_SEARCH_STUB = "error"
    }

}