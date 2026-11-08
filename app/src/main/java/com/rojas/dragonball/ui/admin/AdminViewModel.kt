package com.rojas.dragonball.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rojas.dragonball.AppContainer
import com.rojas.dragonball.data.repository.ItemRepository
import com.rojas.dragonball.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Lo que muestra el panel: cuantos personajes y favoritos hay en Room
data class Totales(val guardados: Int, val favoritos: Int)

class AdminViewModel(
    private val repo: ItemRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Totales>>(UiState.Cargando)
    val estado: StateFlow<UiState<Totales>> = _estado.asStateFlow()

    // Mensaje despues de sincronizar o limpiar
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        cargarTotales()
    }

    private fun cargarTotales() {
        viewModelScope.launch {
            _estado.value = UiState.Exito(Totales(repo.cantidadGuardada(), repo.cantidadFavoritos()))
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            repo.refrescar()
                .onSuccess { n -> _mensaje.value = "Se guardaron $n personajes" }
                .onFailure { _mensaje.value = "No se pudo sincronizar: revisa tu conexión" }
            cargarTotales()
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repo.limpiarCache()
            _mensaje.value = "Se borraron los datos guardados"
            cargarTotales()
        }
    }
}

class AdminViewModelFactory(
    private val contenedor: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AdminViewModel(contenedor.itemRepository) as T
    }
}
