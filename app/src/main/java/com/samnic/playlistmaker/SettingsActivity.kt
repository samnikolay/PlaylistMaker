package com.samnic.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Получаем id кнопки из ресурсов
        val backButton = findViewById<ImageView>(R.id.arrow_back)

        backButton.setOnClickListener {
            val mainsIntent = Intent(this, MainActivity::class.java)
            startActivity(mainsIntent)
        }
    }
}