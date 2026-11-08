package com.rojas.dragonball.data.repository

import com.rojas.dragonball.data.local.ItemDao
import com.rojas.dragonball.data.mapper.aEntity
import com.rojas.dragonball.data.mapper.aModelo
import com.rojas.dragonball.data.remote.ApiService
import com.rojas.dragonball.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    // Room es la fuente de verdad: la lista siempre sale de la base de datos
    override val items: Flow<List<Item>> = dao.todos().map { lista -> lista.map { it.aModelo() } }
    override val favoritos: Flow<List<Item>> = dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    // Retrofit solo actualiza Room
    override suspend fun refrescar(): Result<Int> = try {
        // Si se guarda sin cuidado se pierden los favoritos, por eso se leen antes
        val favoritos = dao.idsFavoritos().toSet()
        val nuevos = api.listar().items
            .map { it.aEntity(esFavorito = it.id in favoritos) }
        dao.guardarTodos(nuevos)
        Result.success(nuevos.size)
    } catch (e: IOException) {          // sin internet
        Result.failure(e)
    } catch (e: HttpException) {        // 404, 500...
        Result.failure(e)
    } catch (e: Exception) {            // JSON con otro formato
        Result.failure(e)
    }

    // El detalle usa el segundo endpoint
    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        Result.success(api.detalle(id).aModelo())
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) = dao.alternarFavorito(id)
    override suspend fun cantidadGuardada(): Int = dao.contar()
    override suspend fun cantidadFavoritos(): Int = dao.contarFavoritos()
    override suspend fun limpiarCache() = dao.borrarTodo()
}
