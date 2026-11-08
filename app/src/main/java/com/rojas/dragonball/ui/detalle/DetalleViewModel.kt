package com.rojas.dragonball.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rojas.dragonball.AppContainer
import com.rojas.dragonball.data.repository.ItemRepository
import com.rojas.dragonball.domain.model.Item
import com.rojas.dragonball.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class DetalleViewModel(
    private val repo: ItemRepository,
    private val id: Int
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            repo.obtenerDetalle(id)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { e ->
                    val mensaje = if (e is IOException) "No hay conexión a internet"
                    else "No se pudo cargar el personaje"
                    _estado.value = UiState.Error(mensaje)
                }
        }
    }
}

// Crea el DetalleViewModel con el repositorio y el id del personaje
class DetalleViewModelFactory(
    private val contenedor: AppContainer,
    private val id: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetalleViewModel(contenedor.itemRepository, id) as T
    }
}
