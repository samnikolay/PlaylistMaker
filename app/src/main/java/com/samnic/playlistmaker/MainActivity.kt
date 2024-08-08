package com.samnic.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Получаем id кнопок из ресурсов
        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaLibraryButton = findViewById<Button>(R.id.media_library_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        // Первый способ - Создаем слушатель для кнопки ПОИСК
        val searchButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)

            }
        }
        // Вызываем слушателя ПОИСК
        searchButton.setOnClickListener(searchButtonClickListener)

        // Второй способ - Создаем слушателя для кнопки МЕДИАТЕКА через люмбду
        val mediaLibraryButtonClickListener: View.OnClickListener = View.OnClickListener {
            val mediaLibraryIntent = Intent(this@MainActivity, MediaLibraryActivity::class.java)
            startActivity(mediaLibraryIntent)
        }
        // Вызываем слушателя МЕДИАТЕКА
        mediaLibraryButton.setOnClickListener(mediaLibraryButtonClickListener)

        // Третий способ - пишем код исполения
        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}