package com.example.fevadisproject

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class Registro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Referencias
        val tabLogin = findViewById<TextView>(R.id.tabLogin)
        val tabRegister = findViewById<TextView>(R.id.tabRegister)
        val tabIndicator = findViewById<View>(R.id.tabIndicator)
        val dniInput = findViewById<EditText>(R.id.dniInput)
        val btnVerificar = findViewById<Button>(R.id.btnVerificarDni)

        // Esperamos a que cargue el layout
        tabLogin.post {

            val indicatorWidth = tabLogin.width
            tabIndicator.layoutParams.width = indicatorWidth

            // Registro seleccionado por defecto → mover indicador
            tabIndicator.translationX = indicatorWidth.toFloat()

            // Animación del rectángulo deslizante
            fun moveIndicator(toX: Float) {
                ObjectAnimator.ofFloat(tabIndicator, "translationX", toX).apply {
                    duration = 250
                    start()
                }
            }

            // Cambio de colores de texto
            @RequiresApi(Build.VERSION_CODES.M)
            fun activateLogin() {
                tabLogin.setTextColor(resources.getColor(android.R.color.black, theme))
                tabRegister.setTextColor(resources.getColor(android.R.color.darker_gray, theme))
            }

            @RequiresApi(Build.VERSION_CODES.M)
            fun activateRegister() {
                tabRegister.setTextColor(resources.getColor(android.R.color.black, theme))
                tabLogin.setTextColor(resources.getColor(android.R.color.darker_gray, theme))
            }

            // Estado inicial
            activateRegister()

            // --- CLICK EN LOGIN → volver a MainActivity ---
            tabLogin.setOnClickListener {
                moveIndicator(0f)
                activateLogin()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            // --- CLICK EN REGISTRO (ya estamos aquí) ---
            tabRegister.setOnClickListener {
                moveIndicator(indicatorWidth.toFloat())
                activateRegister()
            }

        }
    }
}
