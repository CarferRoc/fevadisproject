package com.example.fevadisproject.Pages

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fevadisproject.Adapter.ActividadUsuarioAdapter
import com.example.fevadisproject.Models.Actividad
import com.example.fevadisproject.R
import org.json.JSONObject

class EventsActivity : AppCompatActivity() {

    // --- Filtros ---
    private lateinit var tabTodas: TextView
    private lateinit var tabOcio: TextView
    private lateinit var tabCampamentos: TextView
    private lateinit var tabFormaciones: TextView
    private lateinit var tabTalleres: TextView
    private lateinit var allTabs: List<TextView>

    // Recycler
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: ActividadUsuarioAdapter

    private var listaOriginal = mutableListOf<Actividad>()
    private var listaFiltrada = mutableListOf<Actividad>()

    // --- Navbar icons ---
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
        setContentView(R.layout.activity_events)

        // Cambiar color navbar
        window.navigationBarColor = resources.getColor(R.color.nav_green, theme)

        initUI()
        cargarActividades()
        setupFiltros()
        activateMenu(0)
    }

    private fun initUI() {

        // ---------- FILTERS ----------
        tabTodas = findViewById(R.id.filterTodas)
        tabOcio = findViewById(R.id.filterOcio)
        tabCampamentos = findViewById(R.id.filterCampamentos)
        tabFormaciones = findViewById(R.id.filterFormaciones)
        tabTalleres = findViewById(R.id.filterTalleres)

        allTabs = listOf(tabTodas, tabOcio, tabCampamentos, tabFormaciones, tabTalleres)

        recycler = findViewById(R.id.recyclerActividadesUsuario)
        recycler.layoutManager = LinearLayoutManager(this)

        // ---------- NAVBAR ----------
        navCalendar = findViewById(R.id.navCalendar)
        navChat = findViewById(R.id.navChat)
        navFolder = findViewById(R.id.navFolder)
        navUser = findViewById(R.id.navUser)

        layoutCalendar = findViewById(R.id.navCalendarLayout)
        layoutChat = findViewById(R.id.navChatLayout)
        layoutFolder = findViewById(R.id.navFolderLayout)
        layoutUser = findViewById(R.id.navUserLayout)

        layoutCalendar.setOnClickListener { }
        layoutChat.setOnClickListener {
            startActivity(Intent(this, ChatsActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
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

    // --------------------------------------------------
    //      CARGAR ACTIVIDADES DESDE LA API
    // --------------------------------------------------
    private fun cargarActividades() {

        val url = "http://172.20.10.3/fevadis_api/get_actividades.php"

        val req = StringRequest(
            Request.Method.GET, url,
            { response ->

                try {
                    val json = JSONObject(response)

                    if (json.getString("status") == "ok") {

                        listaOriginal.clear()
                        val arr = json.getJSONArray("actividades")

                        for (i in 0 until arr.length()) {
                            val a = arr.getJSONObject(i)

                            listaOriginal.add(
                                Actividad(
                                    id = a.getInt("id"),
                                    titulo = a.getString("titulo"),
                                    descripcion = a.getString("descripcion"),
                                    categoria = a.getString("categoria"),
                                    fecha = a.getString("fecha"),
                                    plazas = a.getInt("plazas"),
                                    ubicacion = a.getString("ubicacion")
                                )
                            )
                        }

                        listaFiltrada = listaOriginal.toMutableList()
                        actualizarRecycler()

                    }

                } catch (e: Exception) {
                    Toast.makeText(this, "ERROR JSON: ${e.message}", Toast.LENGTH_LONG).show()
                }

            },
            {
                Toast.makeText(this, "Error al cargar actividades", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(req)
    }

    private fun actualizarRecycler() {
        adapter = ActividadUsuarioAdapter(listaFiltrada, this)
        recycler.adapter = adapter
    }

    // --------------------------------------------------
    //                    FILTROS
    // --------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupFiltros() {

        tabTodas.setOnClickListener { aplicarFiltro("Todas"); selectFilter(tabTodas) }
        tabOcio.setOnClickListener { aplicarFiltro("Ocio"); selectFilter(tabOcio) }
        tabCampamentos.setOnClickListener { aplicarFiltro("Campamentos"); selectFilter(tabCampamentos) }
        tabFormaciones.setOnClickListener { aplicarFiltro("Formaciones"); selectFilter(tabFormaciones) }
        tabTalleres.setOnClickListener { aplicarFiltro("Talleres"); selectFilter(tabTalleres) }

        selectFilter(tabTodas)
    }

    private fun aplicarFiltro(cat: String) {

        listaFiltrada = when (cat) {
            "Todas" -> listaOriginal.toMutableList()
            else -> listaOriginal.filter {
                it.categoria.equals(cat, ignoreCase = true)
            }.toMutableList()
        }

        actualizarRecycler()
    }


    // ----------- SELECTOR VISUAL DE TABS -----------
    @RequiresApi(Build.VERSION_CODES.M)
    private fun selectFilter(selected: TextView) {

        allTabs.forEach { tab ->
            if (tab == selected) {
                tab.setBackgroundResource(R.drawable.filter_selected)
                tab.setTypeface(null, Typeface.BOLD)
                tab.setTextColor(resources.getColor(R.color.black, theme))
            } else {
                tab.background = null
                tab.setTypeface(null, Typeface.NORMAL)
                tab.setTextColor(resources.getColor(R.color.gray_700, theme))
            }
        }
    }


    // --------------------------------------------------
    //               NAVBAR ANIMACIONES
    // --------------------------------------------------
    private fun activateMenu(selected: Int) {

        val activeColor = Color.parseColor("#355E3B")

        navCalendar.setColorFilter(Color.WHITE)
        navChat.setColorFilter(Color.WHITE)
        navFolder.setColorFilter(Color.WHITE)
        navUser.setColorFilter(Color.WHITE)

        resetPosition(layoutCalendar)
        resetPosition(layoutChat)
        resetPosition(layoutFolder)
        resetPosition(layoutUser)

        when (selected) {
            0 -> { navCalendar.setColorFilter(activeColor); raiseIcon(layoutCalendar) }
            1 -> { navChat.setColorFilter(activeColor); raiseIcon(layoutChat) }
            2 -> { navFolder.setColorFilter(activeColor); raiseIcon(layoutFolder) }
            3 -> { navUser.setColorFilter(activeColor); raiseIcon(layoutUser) }
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
