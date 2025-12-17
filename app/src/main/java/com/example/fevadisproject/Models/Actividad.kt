package com.example.fevadisproject.Models

data class Actividad(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val fecha: String,
    var plazas: Int,   // ← CAMBIAR A var
    val ubicacion: String
)

