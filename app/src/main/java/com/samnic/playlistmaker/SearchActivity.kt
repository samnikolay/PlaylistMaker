package com.samnic.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var searchField: EditText
    private var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Получаем id кнопки из ресурсов
        searchField = findViewById(R.id.searchField)
        val backButton = findViewById<ImageView>(R.id.arrowBack)
        val clearIcon = findViewById<ImageView>(R.id.clearIcon)
        val testText = findViewById<TextView>(R.id.testText)

        // Кнопка назад
        backButton.setOnClickListener {
            val mainsIntent = Intent(this, MainActivity::class.java)
            startActivity(mainsIntent)
        }

        clearIcon.setOnClickListener {
            searchField.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Тут мне нечего делать пока что
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                testText.setText(s)
                clearIcon.visibility = clearButtonVisibility(s)
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
        outState.putString("searchText", searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchField.setText(savedInstanceState.getString("searchText",""))
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}