package com.example.fevadisproject.Pages.Administrador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fevadisproject.Models.Actividad
import com.example.fevadisproject.R

class ActividadAdminAdapter(
    private val lista: List<Actividad>,
    private val onEdit: (Actividad) -> Unit,
    private val onDelete: (Actividad) -> Unit
) : RecyclerView.Adapter<ActividadAdminAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val titulo: TextView = v.findViewById(R.id.textTitulo)
        val categoria: TextView = v.findViewById(R.id.textCategoria)
        val descripcion: TextView = v.findViewById(R.id.textDescripcion)
        val fecha: TextView = v.findViewById(R.id.textFecha)
        val plazas: TextView = v.findViewById(R.id.textPlazas)
        val ubicacion: TextView = v.findViewById(R.id.textUbicacion)
        val btnEditar: Button = v.findViewById(R.id.btnEditar)
        val btnBorrar: Button = v.findViewById(R.id.btnBorrar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad_admin, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val a = lista[pos]

        h.titulo.text = a.titulo
        h.categoria.text = a.categoria
        h.descripcion.text = a.descripcion
        h.fecha.text = a.fecha
        h.plazas.text = "${a.plazas} plazas"
        h.ubicacion.text = a.ubicacion

        h.btnEditar.setOnClickListener { onEdit(a) }
        h.btnBorrar.setOnClickListener { onDelete(a) }
    }
}
