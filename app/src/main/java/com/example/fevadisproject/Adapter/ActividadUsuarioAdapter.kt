package com.example.fevadisproject.Adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fevadisproject.Models.Actividad
import com.example.fevadisproject.R
import org.json.JSONObject

class ActividadUsuarioAdapter(
    private var lista: MutableList<Actividad>,
    private val context: Context
) : RecyclerView.Adapter<ActividadUsuarioAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val titulo = v.findViewById<TextView>(R.id.textTitulo)
        val categoria = v.findViewById<TextView>(R.id.textCategoria)
        val descripcion = v.findViewById<TextView>(R.id.textDescripcion)
        val fecha = v.findViewById<TextView>(R.id.textFecha)
        val plazas = v.findViewById<TextView>(R.id.textPlazas)
        val ubicacion = v.findViewById<TextView>(R.id.textUbicacion)
        val btnInscribirse = v.findViewById<Button>(R.id.btnInscribirse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad_usuario, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val a = lista[pos]

        h.titulo.text = a.titulo
        h.descripcion.text = a.descripcion
        h.categoria.text = a.categoria
        h.fecha.text = a.fecha
        h.ubicacion.text = a.ubicacion
        h.plazas.text = "${a.plazas} plazas disponibles"

        if (a.plazas <= 0) {
            h.btnInscribirse.text = "Completo"
            h.btnInscribirse.isEnabled = false
        }

        h.btnInscribirse.setOnClickListener {
            inscribirseActividad(a, pos)
        }
    }


    // ========================================================
    //     INSCRIPCIÓN A UNA ACTIVIDAD
    // ========================================================
    private fun inscribirseActividad(actividad: Actividad, index: Int) {

        val prefs: SharedPreferences = context.getSharedPreferences("userData", Context.MODE_PRIVATE)
        val dni = prefs.getString("dni", null)

        if (dni == null) {
            Toast.makeText(context, "Error: no se encontró tu sesión", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://172.20.10.3/fevadis_api/inscribir_actividad.php"

        val req = object : StringRequest(
            Method.POST, url,
            { response ->

                val json = JSONObject(response)

                when (json.getString("status")) {

                    "ok" -> {
                        Toast.makeText(context, "Inscripción completada ✔", Toast.LENGTH_LONG).show()

                        lista[index].plazas -= 1
                        notifyItemChanged(index)
                    }

                    "already" -> {
                        Toast.makeText(context, "Ya estás inscrito en esta actividad ❗", Toast.LENGTH_SHORT).show()
                    }

                    "full" -> {
                        Toast.makeText(context, "No quedan plazas disponibles ❗", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Toast.makeText(context, "Error al inscribirse ❌", Toast.LENGTH_SHORT).show()
                    }
                }

            },
            {
                Toast.makeText(context, "Error al conectar ❌", Toast.LENGTH_SHORT).show()
            }
        ) {

            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "dni" to dni,
                    "actividad_id" to actividad.id.toString()
                )
            }
        }

        Volley.newRequestQueue(context).add(req)
    }
}
