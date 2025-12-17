package com.example.fevadisproject.Pages.Administrador

data class AdminUser(
    val nombre: String,
    val apellidos: String,
    val nacimiento: String,
    val correo: String,
    val dni: String,
    val puntos: Int,
    var rol: String
)

