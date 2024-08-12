package com.samnic.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Получаем id кнопки из ресурсов
        val backButton = findViewById<ImageView>(R.id.arrowBack)
        val shareButton = findViewById<TextView>(R.id.shareButton)
        val supportButton = findViewById<TextView>(R.id.supportButton)
        val forwardButton = findViewById<TextView>(R.id.forwardButton)

        // Кнопка назад
        backButton.setOnClickListener {
            val mainsIntent = Intent(this, MainActivity::class.java)
            startActivity(mainsIntent)
        }

        // Кнопка поделиться
        shareButton.setOnClickListener {
            val shareText = getString(R.string.share_text)

            val intent = Intent().apply {
                action = Intent.ACTION_SEND // Тип экшена
                putExtra(Intent.EXTRA_TEXT, shareText) // Текст сообщения
                type = "text/plain" // Тип содержания
            }

            startActivity(Intent.createChooser(intent, null));
        }

        supportButton.setOnClickListener{
            val title = getString(R.string.support_email_title)
            val message = getString(R.string.support_email_text)
            val myEmail = getString(R.string.my_email)

            val supportIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(myEmail))
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(supportIntent)
        }

        forwardButton.setOnClickListener{
            val url = getString(R.string.terms_of_use_link)
            val forwardIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(forwardIntent)

        }

    }
}