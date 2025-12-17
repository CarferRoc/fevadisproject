package com.example.fevadisproject.Pages.Administrador

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fevadisproject.Models.Actividad
import com.example.fevadisproject.R
import org.json.JSONObject

class Administrador : AppCompatActivity() {

    // ---------- TABS ----------
    private lateinit var tabUsers: TextView
    private lateinit var tabInscripciones: TextView
    private lateinit var tabActividades: TextView
    private lateinit var tabDni: TextView

    // ---------- LAYOUTS ----------
    private lateinit var layoutUsers: LinearLayout
    private lateinit var layoutDni: LinearLayout
    private lateinit var layoutActividades: LinearLayout

    // ---------- USERS ----------
    private lateinit var recyclerUsers: RecyclerView
    private lateinit var usersAdapter: UserAdminAdapter
    private val usersList = mutableListOf<AdminUser>()

    // ---------- ACTIVIDADES ----------
    private lateinit var recyclerActividades: RecyclerView
    private lateinit var actividadesAdapter: ActividadAdminAdapter
    private lateinit var textNoActivities: TextView
    private lateinit var btnNuevaActividad: Button
    private val listaActividades = mutableListOf<Actividad>()



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrador)

        initUI()
        setupTabs()
        setupRecyclerUsers()

        getUsersFromDB()

    }

    // =====================================================
    // INIT
    // =====================================================
    private fun initUI() {

        tabUsers = findViewById(R.id.tabUsers)
        tabInscripciones = findViewById(R.id.tabInscripciones)
        tabActividades = findViewById(R.id.tabActividades)
        tabDni = findViewById(R.id.tabDni)

        layoutUsers = findViewById(R.id.layoutUsers)
        layoutDni = findViewById(R.id.layoutDni)
        layoutActividades = findViewById(R.id.layoutActividades)

        recyclerUsers = findViewById(R.id.recyclerUsers)
        recyclerActividades = findViewById(R.id.recyclerActividades)

        textNoActivities = findViewById(R.id.textNoActividades)
        btnNuevaActividad = findViewById(R.id.btnNuevaActividad)

        btnNuevaActividad.setOnClickListener {
            Toast.makeText(this, "Crear actividad (pendiente UI)", Toast.LENGTH_SHORT).show()
        }
    }

    // =====================================================
    // TABS
    // =====================================================
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupTabs() {

        tabUsers.setOnClickListener {
            selectTab(tabUsers)
            showOnly(layoutUsers)
        }

        tabInscripciones.setOnClickListener {
            selectTab(tabInscripciones)
            Toast.makeText(this, "Inscripciones pendiente", Toast.LENGTH_SHORT).show()
        }

        tabActividades.setOnClickListener {
            selectTab(tabActividades)
            showOnly(layoutActividades)
            cargarActividades()
        }

        tabDni.setOnClickListener {
            selectTab(tabDni)
            showOnly(layoutDni)
        }

        // 👉 Tab por defecto
        selectTab(tabUsers)
        showOnly(layoutUsers)
    }

    private fun showOnly(layout: View) {
        layoutUsers.visibility = View.GONE
        layoutDni.visibility = View.GONE
        layoutActividades.visibility = View.GONE
        layout.visibility = View.VISIBLE
    }

    // =====================================================
    // USERS
    // =====================================================
    private fun setupRecyclerUsers() {
        recyclerUsers.layoutManager = LinearLayoutManager(this)
        usersAdapter = UserAdminAdapter(usersList, this)
        recyclerUsers.adapter = usersAdapter
    }

    private fun getUsersFromDB() {

        val url = "http://172.20.10.3/fevadis_api/get_users.php"

        val req = object : StringRequest(
            Request.Method.POST,
            url,
            { response ->
                val json = JSONObject(response)

                if (json.getString("status") == "ok") {
                    usersList.clear()
                    val arr = json.getJSONArray("usuarios")

                    for (i in 0 until arr.length()) {
                        val u = arr.getJSONObject(i)
                        usersList.add(
                            AdminUser(
                                u.getString("nombre"),
                                u.getString("apellidos"),
                                u.getString("nacimiento"),
                                u.getString("correo"),
                                u.getString("dni"),
                                u.getInt("puntos"),
                                u.getString("rol")
                            )
                        )
                    }

                    usersAdapter.notifyDataSetChanged()
                }
            },
            { Toast.makeText(this, "Error usuarios", Toast.LENGTH_SHORT).show() }
        ) {
            override fun getParams() = hashMapOf("secret" to "fevadis_admin_key")
        }

        Volley.newRequestQueue(this).add(req)
    }
    private fun selectTab(selected: TextView) {
        val tabs = listOf(tabUsers, tabInscripciones, tabActividades, tabDni)

        tabs.forEach { tab ->
            if (tab == selected) {
                tab.setTypeface(null, android.graphics.Typeface.BOLD)
                tab.setTextColor(resources.getColor(android.R.color.black, theme))
            } else {
                tab.setTypeface(null, android.graphics.Typeface.NORMAL)
                tab.setTextColor(resources.getColor(android.R.color.darker_gray, theme))
            }
        }
    }

    // =====================================================
    // ACTIVIDADES
    // =====================================================
    private fun cargarActividades() {

        val url = "http://172.20.10.3/fevadis_api/get_actividades.php"

        val req = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                val json = JSONObject(response)
                listaActividades.clear()

                if (json.getString("status") == "ok") {
                    val arr = json.getJSONArray("actividades")
                    for (i in 0 until arr.length()) {
                        val a = arr.getJSONObject(i)
                        listaActividades.add(
                            Actividad(
                                a.getInt("id"),
                                a.getString("titulo"),
                                a.getString("descripcion"),
                                a.getString("categoria"),
                                a.getString("fecha"),
                                a.getInt("plazas"),
                                a.getString("ubicacion")
                            )
                        )
                    }
                }

                textNoActivities.visibility =
                    if (listaActividades.isEmpty()) View.VISIBLE else View.GONE

                actividadesAdapter = ActividadAdminAdapter(
                    listaActividades,
                    onEdit = { Toast.makeText(this, "Editar ${it.titulo}", Toast.LENGTH_SHORT).show() },
                    onDelete = { Toast.makeText(this, "Eliminar ${it.id}", Toast.LENGTH_SHORT).show() }
                )

                recyclerActividades.layoutManager = LinearLayoutManager(this)
                recyclerActividades.adapter = actividadesAdapter
            },
            {
                Toast.makeText(this, "Error actividades", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(req)
    }

}
