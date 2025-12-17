package com.example.fevadisproject.Pages

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.fevadisproject.MainActivity
import com.example.fevadisproject.Pages.Administrador.Administrador
import com.example.fevadisproject.R

class ProfileActivity : AppCompatActivity() {

    private lateinit var layoutCalendar: LinearLayout
    private lateinit var layoutChat: LinearLayout
    private lateinit var layoutFolder: LinearLayout
    private lateinit var layoutUser: LinearLayout

    private lateinit var navCalendar: ImageView
    private lateinit var navChat: ImageView
    private lateinit var navFolder: ImageView
    private lateinit var navUser: ImageView

    private lateinit var rowInfo: LinearLayout
    private lateinit var rowUpload: LinearLayout
    private lateinit var rowActivity: LinearLayout
    private lateinit var rowAdmin: LinearLayout
    private lateinit var rowLogout: LinearLayout

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        window.navigationBarColor = resources.getColor(R.color.nav_green, theme)

        // NAVBAR
        layoutCalendar = findViewById(R.id.navCalendarLayout)
        layoutChat = findViewById(R.id.navChatLayout)
        layoutFolder = findViewById(R.id.navFolderLayout)
        layoutUser = findViewById(R.id.navUserLayout)

        navCalendar = findViewById(R.id.navCalendar)
        navChat = findViewById(R.id.navChat)
        navFolder = findViewById(R.id.navFolder)
        navUser = findViewById(R.id.navUser)

        // Activar icono perfil
        activateMenu(3)

        // Cargar datos del usuario
        loadUserInfo()

        // Ajustes
        rowInfo = findViewById(R.id.rowSettingInfo)
        rowUpload = findViewById(R.id.rowSettingUpload)
        rowActivity = findViewById(R.id.rowSettingActivity)
        rowAdmin = findViewById(R.id.rowSettingAdmin)
        rowLogout = findViewById(R.id.rowSettingLogout)

        // 🔥 Ocultar panel admin si NO es administrador
        val prefs = getSharedPreferences("userData", MODE_PRIVATE)
        val rol = prefs.getString("rol", "")

        if (rol != "administrador") {
            rowAdmin.visibility = LinearLayout.GONE
        }

        setupListeners()
    }

    // =====================================================
    // Cargar nombre desde SharedPreferences
    // =====================================================
    private fun loadUserInfo() {
        val prefs = getSharedPreferences("userData", MODE_PRIVATE)

        val nombre = prefs.getString("nombre", "") ?: ""
        val apellidos = prefs.getString("apellidos", "") ?: ""
        val puntos = prefs.getInt("puntos", 0)

        // Nombre completo
        val finalName = when {
            nombre.isNotBlank() && apellidos.isNotBlank() -> "$nombre $apellidos"
            nombre.isNotBlank() -> nombre
            else -> "Usuario"
        }

        findViewById<TextView>(R.id.userName).text = finalName

        // 🔥 Poner puntos correctamente
        val puntosText = findViewById<TextView>(R.id.txtPuntos)

        puntosText.text = if (puntos == 1) {
            "1 punto"
        } else {
            "$puntos puntos"
        }
    }


    // =====================================================
    // LISTENERS (Ajustes + Navbar)
    // =====================================================
    private fun setupListeners() {

        // Información personal
        rowInfo.setOnClickListener { /* abrir activity */ }

        // Subir archivos
        rowUpload.setOnClickListener { /* abrir activity */ }

        // Actividad
        rowActivity.setOnClickListener { /* abrir activity */ }

        // Panel admin
        rowAdmin.setOnClickListener {
            val prefs = getSharedPreferences("userData", MODE_PRIVATE)
            val rol = prefs.getString("rol", "")

            if (rol == "administrador") {
                 startActivity(Intent(this, Administrador::class.java))
            }
        }

        // Logout
        rowLogout.setOnClickListener {

            val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)
            val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create()

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            dialog.window?.attributes?.windowAnimations = R.style.DialogFadeScale

            dialog.show()

            // Botones
            val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)
            val btnLogout = dialogView.findViewById<Button>(R.id.btnLogout)

            btnCancelar.setOnClickListener {
                dialog.dismiss()
            }

            btnLogout.setOnClickListener {
                val prefs = getSharedPreferences("userData", MODE_PRIVATE)
                prefs.edit().clear().apply()

                dialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }


        // NAVBAR
        layoutCalendar.setOnClickListener {
            startActivity(Intent(this, EventsActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }

        layoutChat.setOnClickListener {
            startActivity(Intent(this, ChatsActivity::class.java))
            overridePendingTransition(0, 0)
            finish() }

        layoutFolder.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
            overridePendingTransition(0, 0)
            finish() }

        layoutUser.setOnClickListener {
            // ya estás en perfil
        }
    }

    // =====================================================
    // NAVBAR Animación
    // =====================================================
    private fun activateMenu(selected: Int) {

        val activeColor = Color.parseColor("#5F8D63")

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
            }.start()
    }

    private fun resetPosition(layout: LinearLayout) {
        layout.animate()
            .translationY(0f)
            .setDuration(150)
            .start()
    }
}
