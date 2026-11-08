package com.rojas.dragonball.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla "items" de Room: un personaje guardado en el celular
@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val raza: String,
    val ki: String,
    val afiliacion: String,
    val imagenUrl: String?,
    val esFavorito: Boolean = false
)
