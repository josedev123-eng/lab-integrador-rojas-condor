package com.rojas.dragonball.domain.model

// Modelo que usa la UI: un personaje de Dragon Ball
data class Item(
    val id: Int,
    val nombre: String,
    val raza: String,
    val ki: String,
    val afiliacion: String,
    val imagenUrl: String?,
    val esFavorito: Boolean = false,
    // Estos datos solo llegan con el endpoint de detalle
    val descripcion: String = "",
    val planeta: String = "",
    val transformaciones: List<String> = emptyList()
)
