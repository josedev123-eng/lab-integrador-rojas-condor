package com.rojas.dragonball.ui.favoritos

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

class FavoritosViewModel(
    private val repo: ItemRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    init {
        // Los favoritos salen de Room, se actualizan solos al marcar o desmarcar
        viewModelScope.launch {
            repo.favoritos.collect { lista ->
                _estado.value = UiState.Exito(lista)
            }
        }
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch { repo.alternarFavorito(id) }
    }
}

class FavoritosViewModelFactory(
    private val contenedor: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoritosViewModel(contenedor.itemRepository) as T
    }
}
