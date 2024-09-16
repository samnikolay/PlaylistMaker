package com.samnic.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
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

    private val iTunseApi = retrofit.create(ITunesApi::class.java)

    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(tracks)

    private lateinit var searchField: EditText
    private lateinit var backButton: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var trackListView: RecyclerView


    private var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchField = findViewById(R.id.searchField)
        backButton = findViewById(R.id.arrowBack)
        clearButton = findViewById(R.id.clearIcon)
        trackListView = findViewById(R.id.trackList)

        trackListView.adapter = trackAdapter

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchField.setText("")
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchField.text.isNotEmpty()) {
                    iTunseApi.searchTrack(searchField.text.toString(),SEARCH_TACK_LIMIT).enqueue(object :
                        Callback<TrackResponse> {
                        override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
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
                            showStub(ERROR_SEARCH_STUB)
                        }

                    })
                }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchField.setText(savedInstanceState.getString(EXTRA_SEARCH_TEXT,""))
    }

    private fun clearButtonVisibility(s: CharSequence?, clearButton: ImageView) {
        clearButton.isVisible = !s.isNullOrEmpty()
    }

    private fun showStub(type: String) {
        when(type) {
            EMPTY_SEARCH_STUB -> {
                Toast.makeText(applicationContext,getString(R.string.empty_search),Toast.LENGTH_LONG).show()
            }
            ERROR_SEARCH_STUB -> {
                Toast.makeText(applicationContext,getString(R.string.error_search_title),Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val EXTRA_SEARCH_TEXT = "searchText"
        const val BASE_URL = "https://itunes.apple.com"
        const val SEARCH_TACK_LIMIT = 25
        const val EMPTY_SEARCH_STUB = "empty"
        const val ERROR_SEARCH_STUB = "error"
    }

}