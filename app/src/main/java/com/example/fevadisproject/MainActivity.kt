package com.example.fevadisproject

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fevadisproject.Pages.ChatsActivity
import com.example.fevadisproject.Pages.EventsActivity
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    // === VIEWS USADAS EN REGISTRO ===
    private lateinit var layoutLogin: View
    private lateinit var layoutRegister: View
    private lateinit var layoutPolicy: View
    private lateinit var layoutExtra1: View
    private lateinit var layoutExtra2: View
    private lateinit var checkPolicy: ImageView
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ===============================
        //      REFERENCIAS LOGIN
        // ===============================
        val tabLogin = findViewById<TextView>(R.id.tabLogin)
        val tabRegister = findViewById<TextView>(R.id.tabRegister)
        val tabIndicator = findViewById<View>(R.id.tabIndicator)

        layoutLogin = findViewById(R.id.layoutLogin)
        layoutRegister = findViewById(R.id.layoutRegister)

        val dniInput = findViewById<EditText>(R.id.dniInput)
        val passInput = findViewById<EditText>(R.id.passInput)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // ===============================
        //     REFERENCIAS REGISTRO
        // ===============================
        val dniRegister = findViewById<EditText>(R.id.dniRegister)
        val btnVerificarDni = findViewById<Button>(R.id.btnVerificarDni)

        layoutPolicy = findViewById(R.id.layoutPolicy)
        layoutExtra1 = findViewById(R.id.layoutExtra1)
        layoutExtra2 = findViewById(R.id.layoutExtra2)
        checkPolicy = findViewById(R.id.checkPolicy)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        val nombreInput = findViewById<EditText>(R.id.nombreInput)
        val apellidosInput = findViewById<EditText>(R.id.apellidosInput)
        val nacimientoInput = findViewById<EditText>(R.id.nacimientoInput)
        val correoRegInput = findViewById<EditText>(R.id.correoRegInput)
        val passRegInput = findViewById<EditText>(R.id.passRegInput)

        // Ocultar secciones al inicio
        layoutPolicy.visibility = View.GONE
        layoutExtra1.visibility = View.GONE
        layoutExtra2.visibility = View.GONE
        btnRegistrar.visibility = View.GONE

        layoutLogin.visibility = View.VISIBLE
        layoutRegister.visibility = View.GONE

        // ===============================
        //        ANIMACIÓN TABS
        // ===============================
        tabLogin.post {
            val w = tabLogin.width
            tabIndicator.layoutParams.width = w
            tabIndicator.translationX = 0f

            fun move(to: Float) {
                ObjectAnimator.ofFloat(tabIndicator, "translationX", to).apply {
                    duration = 250
                    start()
                }
            }

            fun showLogin() {
                layoutLogin.visibility = View.VISIBLE
                layoutRegister.visibility = View.GONE
                tabLogin.setTextColor(getColor(android.R.color.black))
                tabRegister.setTextColor(getColor(android.R.color.darker_gray))
            }

            fun showRegister() {
                layoutLogin.visibility = View.GONE
                layoutRegister.visibility = View.VISIBLE
                tabRegister.setTextColor(getColor(android.R.color.black))
                tabLogin.setTextColor(getColor(android.R.color.darker_gray))
            }

            showLogin()

            tabLogin.setOnClickListener {
                move(0f)
                showLogin()
            }

            tabRegister.setOnClickListener {
                move(w.toFloat())
                showRegister()
            }
        }

        // ===============================
        //             LOGIN
        // ===============================
        btnLogin.setOnClickListener {
            val dni = dniInput.text.toString().trim()
            val pass = passInput.text.toString().trim()

            if (dni.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUsuario(dni, pass)
        }

        // ===============================
        //       VERIFICAR DNI (Registro)
        // ===============================
        btnVerificarDni.setOnClickListener {
            val dni = dniRegister.text.toString().trim()

            if (dni.isEmpty()) {
                Toast.makeText(this, "Introduce tu DNI", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            verificarDNI(dni)
        }

        // ===============================
        //          BOTÓN REGISTRAR
        // ===============================
        btnRegistrar.setOnClickListener {

            val dni = dniRegister.text.toString()
            val nombre = nombreInput.text.toString()
            val apellidos = apellidosInput.text.toString()
            val fecha = nacimientoInput.text.toString()
            val correo = correoRegInput.text.toString()
            val pass = passRegInput.text.toString()

            if (nombre.isEmpty() || apellidos.isEmpty() || fecha.isEmpty() ||
                correo.isEmpty() || pass.isEmpty()
            ) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registrarUsuario(dni, nombre, apellidos, fecha, correo, pass)
        }

        // ===============================
        //  CHECKBOX POLÍTICA DE DATOS
        // ===============================
        checkPolicy.setOnClickListener {
            val checked = checkPolicy.isSelected
            checkPolicy.isSelected = !checked

            if (!checked) {
                checkPolicy.setImageResource(R.drawable.check_on)
                layoutExtra1.visibility = View.VISIBLE
                layoutExtra2.visibility = View.VISIBLE
                btnRegistrar.visibility = View.VISIBLE

            } else {
                checkPolicy.setImageResource(R.drawable.check_off)
                layoutExtra1.visibility = View.GONE
                layoutExtra2.visibility = View.GONE
                btnRegistrar.visibility = View.GONE
            }
        }
    }

    // ============================================================
    //                           LOGIN
    // ============================================================
    private fun loginUsuario(dni: String, password: String) {
        val url = "http://172.20.10.3/fevadis_api/login.php"

        val request = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                try {
                    val json = JSONObject(response)
                    if (json.getString("status") == "ok") {

                        val u = json.getJSONObject("usuario")
                        val prefs = getSharedPreferences("userData", MODE_PRIVATE).edit()

                        prefs.putString("dni", u.getString("dni"))
                        prefs.putString("nombre", u.getString("nombre"))
                        prefs.putString("apellidos", u.getString("apellidos"))
                        prefs.putString("correo", u.getString("correo"))
                        prefs.putString("rol", u.getString("rol"))
                        prefs.putInt("puntos", u.getInt("puntos"))
                        prefs.apply()

                        startActivity(Intent(this, ChatsActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(this, "DNI o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Error JSON", Toast.LENGTH_SHORT).show()
                }
            },
            { Toast.makeText(this, "Error al conectar", Toast.LENGTH_SHORT).show() }
        ) {
            override fun getParams(): MutableMap<String, String> =
                hashMapOf("dni" to dni, "password" to password)
        }

        Volley.newRequestQueue(this).add(request)
    }

    // ============================================================
    //                 VERIFICAR DNI AUTORIZADO
    // ============================================================
    private fun verificarDNI(dni: String) {
        val url = "http://172.20.10.3/fevadis_api/check_dni.php"

        val req = object : StringRequest(
            Method.POST, url,
            { response ->
                val json = JSONObject(response)

                when (json.getString("status")) {

                    "ok" -> {
                        mostrarModalDniOK {
                            layoutPolicy.visibility = View.VISIBLE
                        }
                    }

                    "denied" -> mostrarModalDniDenied()

                    else -> Toast.makeText(this, "Error en servidor", Toast.LENGTH_SHORT).show()
                }

            },
            { Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show() }
        ) {
            override fun getParams(): MutableMap<String, String> =
                hashMapOf("dni" to dni)
        }

        Volley.newRequestQueue(this).add(req)
    }

    // ============================================================
    //                 MODALES PERSONALIZADOS
    // ============================================================
    private fun mostrarModalDniOK(onContinue: () -> Unit) {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.dialog_dni_ok)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnContinue = dialog.findViewById<Button>(R.id.btnContinuar)

        btnContinue.setOnClickListener {
            dialog.dismiss()
            onContinue()
        }

        dialog.show()
    }

    private fun mostrarModalDniDenied() {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.dialog_dni_denied)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnOk = dialog.findViewById<Button>(R.id.btnAceptar)
        btnOk.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    // ============================================================
    //                 REGISTRO COMPLETO USUARIO
    // ============================================================
    private fun registrarUsuario(
        dni: String,
        nombre: String,
        apellidos: String,
        fecha: String,
        correo: String,
        pass: String
    ) {

        val url = "http://172.20.10.3/fevadis_api/registrar_usuario.php"

        val request = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val json = JSONObject(response)

                    when (json.getString("status")) {
                        "ok" -> {
                            Toast.makeText(this, "Registro completado ✔", Toast.LENGTH_LONG).show()
                            layoutRegister.visibility = View.GONE
                            layoutLogin.visibility = View.VISIBLE
                        }

                        "invalid_dni" ->
                            mostrarModalDniDenied()

                        "exists" ->
                            Toast.makeText(this, "Este DNI ya está registrado ❌", Toast.LENGTH_LONG).show()

                        else ->
                            Toast.makeText(this, "Error al registrar ❌", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    Toast.makeText(this, "ERROR JSON: ${e.message}", Toast.LENGTH_LONG).show()
                }
            },
            {
                Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> =
                hashMapOf(
                    "dni" to dni,
                    "nombre" to nombre,
                    "apellidos" to apellidos,
                    "fecha" to fecha,
                    "correo" to correo,
                    "password" to pass
                )
        }

        Volley.newRequestQueue(this).add(request)
    }
}
