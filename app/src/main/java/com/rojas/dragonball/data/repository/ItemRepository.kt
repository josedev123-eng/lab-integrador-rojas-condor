package com.rojas.dragonball.data.repository

import com.rojas.dragonball.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    val items: Flow<List<Item>>           // sale de Room
    val favoritos: Flow<List<Item>>
    suspend fun refrescar(): Result<Int>      // Retrofit -> Room; devuelve cuantos guardo
    suspend fun obtenerDetalle(id: Int): Result<Item>
    suspend fun alternarFavorito(id: Int)
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
}
