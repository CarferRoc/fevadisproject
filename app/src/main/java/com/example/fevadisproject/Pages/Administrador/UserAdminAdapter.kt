package com.example.fevadisproject.Pages.Administrador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fevadisproject.R

class UserAdminAdapter(
    private val users: MutableList<AdminUser>,
    private val context: Context
) : RecyclerView.Adapter<UserAdminAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombre)
        val txtNacimiento: TextView = view.findViewById(R.id.txtNacimiento)
        val txtCorreo: TextView = view.findViewById(R.id.txtCorreo)
        val txtDni: TextView = view.findViewById(R.id.txtDni)
        val txtPuntos: TextView = view.findViewById(R.id.txtPuntos)

        val spinnerRol: Spinner = view.findViewById(R.id.spinnerRol)
        val btnGuardar: Button = view.findViewById(R.id.btnGuardar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_user, parent, false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val u = users[position]

        // RELLENAR CAMPOS
        holder.txtNombre.text = u.nombre
        holder.txtNacimiento.text = u.nacimiento
        holder.txtCorreo.text = u.correo
        holder.txtDni.text = u.dni
        holder.txtPuntos.text = u.puntos.toString()

        // CONFIGURAR SPINNER
        val roles = arrayOf("administrador", "editor", "voluntario")

        val adapterSpinner = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_item,
            roles
        )
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.spinnerRol.adapter = adapterSpinner

        // Seleccionar el rol actual
        val index = roles.indexOf(u.rol)
        if (index >= 0) holder.spinnerRol.setSelection(index)

        // Cuando cambia el spinner → actualizar el objeto
        holder.spinnerRol.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                u.rol = roles[pos]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // GUARDAR CAMBIOS
        holder.btnGuardar.setOnClickListener {
            guardarRolEnServidor(u, holder)
        }
    }

    override fun getItemCount(): Int = users.size


    // ============================================================
    //            PETICIÓN PARA GUARDAR ROL EN EL SERVIDOR
    // ============================================================
    private fun guardarRolEnServidor(user: AdminUser, holder: UserViewHolder) {

        val url = "http://172.20.10.3/fevadis_api/update_role.php"

        val request = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(context, "Rol actualizado correctamente", Toast.LENGTH_SHORT).show()
            },
            {
                Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "dni" to user.dni,
                    "rol" to user.rol,
                    "secret" to "fevadis_admin_key"
                )
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}
