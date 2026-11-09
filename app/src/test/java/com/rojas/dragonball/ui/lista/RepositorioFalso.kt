package com.rojas.dragonball.ui.lista

import com.rojas.dragonball.data.repository.ItemRepository
import com.rojas.dragonball.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.io.IOException

// Repositorio falso: no usa internet ni Room, nosotros decidimos que responde.
// datosApi = null simula que no hay internet.
class RepositorioFalso(
    private val datosApi: List<Item>?,
    datosGuardados: List<Item> = emptyList()
) : ItemRepository {

    private val guardados = MutableStateFlow(datosGuardados)

    override val items: Flow<List<Item>> = guardados
    override val favoritos: Flow<List<Item>> = guardados.map { lista -> lista.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> {
        if (datosApi == null) return Result.failure(IOException("Sin internet"))
        guardados.value = datosApi
        return Result.success(datosApi.size)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> {
        val item = guardados.value.find { it.id == id }
        return if (item != null) Result.success(item) else Result.failure(IOException("Sin internet"))
    }

    override suspend fun alternarFavorito(id: Int) {
        guardados.value = guardados.value.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it }
    }

    override suspend fun cantidadGuardada(): Int = guardados.value.size
    override suspend fun cantidadFavoritos(): Int = guardados.value.count { it.esFavorito }
    override suspend fun limpiarCache() {
        guardados.value = emptyList()
    }
}
