package com.example.fevadisproject.Pages

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fevadisproject.R
import android.graphics.Color

class ChatsActivity : AppCompatActivity() {
    private lateinit var navCalendar: ImageView
    private lateinit var navChat: ImageView
    private lateinit var navFolder: ImageView
    private lateinit var navUser: ImageView

    private lateinit var layoutCalendar: LinearLayout
    private lateinit var layoutChat: LinearLayout
    private lateinit var layoutFolder: LinearLayout
    private lateinit var layoutUser: LinearLayout

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        window.navigationBarColor = resources.getColor(R.color.nav_green, theme)

        // Navbar icons
        navCalendar = findViewById(R.id.navCalendar)
        navChat = findViewById(R.id.navChat)
        navFolder = findViewById(R.id.navFolder)
        navUser = findViewById(R.id.navUser)

        layoutCalendar = findViewById(R.id.navCalendarLayout)
        layoutChat = findViewById(R.id.navChatLayout)
        layoutFolder = findViewById(R.id.navFolderLayout)
        layoutUser = findViewById(R.id.navUserLayout)

        activateMenu(1)  // Chat icon activo

        layoutCalendar.setOnClickListener {
            startActivity(Intent(this, EventsActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        layoutChat.setOnClickListener {
            // Ya estás aquí
        }

        layoutFolder.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        layoutUser.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }
    }

    private fun activateMenu(selected: Int) {

        val activeColor = Color.parseColor("#355E3B")

        // Reset colors
        navCalendar.setColorFilter(Color.WHITE)
        navChat.setColorFilter(Color.WHITE)
        navFolder.setColorFilter(Color.WHITE)
        navUser.setColorFilter(Color.WHITE)

        resetPosition(layoutCalendar)
        resetPosition(layoutChat)
        resetPosition(layoutFolder)
        resetPosition(layoutUser)

        when (selected) {
            0 -> {
                navCalendar.setColorFilter(activeColor)
                raiseIcon(layoutCalendar)
            }
            1 -> {
                navChat.setColorFilter(activeColor)
                raiseIcon(layoutChat)
            }
            2 -> {
                navFolder.setColorFilter(activeColor)
                raiseIcon(layoutFolder)
            }
            3 -> {
                navUser.setColorFilter(activeColor)
                raiseIcon(layoutUser)
            }
        }
    }

    private fun raiseIcon(layout: LinearLayout) {

        layout.animate()
            .translationY(-14f)
            .setDuration(120)
            .withEndAction {

                layout.animate()
                    .translationY(-8f)
                    .setDuration(120)
                    .start()
            }
            .start()
    }

    private fun resetPosition(layout: LinearLayout) {
        layout.animate()
            .translationY(0f)
            .setDuration(150)
            .start()
    }
}