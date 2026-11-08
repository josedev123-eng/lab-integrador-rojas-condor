package com.rojas.dragonball.data.repository

import com.rojas.dragonball.data.mapper.aModelo
import com.rojas.dragonball.data.remote.ApiService
import com.rojas.dragonball.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val api: ApiService
) : ItemRepository {

    // Por ahora la lista se guarda en memoria. En H9 saldra de Room.
    private val _items = MutableStateFlow<List<Item>>(emptyList())

    override val items: Flow<List<Item>> = _items
    override val favoritos: Flow<List<Item>> = _items.map { lista -> lista.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> = try {
        val nuevos = api.listar().items.map { it.aModelo() }
        _items.value = nuevos
        Result.success(nuevos.size)
    } catch (e: IOException) {          // sin internet
        Result.failure(e)
    } catch (e: HttpException) {        // 404, 500...
        Result.failure(e)
    } catch (e: Exception) {            // JSON con otro formato
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        Result.success(api.detalle(id).aModelo())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) {
        _items.value = _items.value.map {
            if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
        }
    }

    override suspend fun cantidadGuardada(): Int = _items.value.size
    override suspend fun cantidadFavoritos(): Int = _items.value.count { it.esFavorito }

    override suspend fun limpiarCache() {
        _items.value = emptyList()
    }
}
