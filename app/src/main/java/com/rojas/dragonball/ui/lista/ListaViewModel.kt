package com.rojas.dragonball.ui.lista

import androidx.lifecycle.ViewModel
import com.rojas.dragonball.domain.model.Item
import com.rojas.dragonball.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListaViewModel : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    init {
        // Por ahora datos de prueba, luego vendran de la API
        _estado.value = UiState.Exito(datosDePrueba)
    }

    private val datosDePrueba: List<Item>
        get() = listOf(
            Item(1, "Goku", "Saiyan", "60.000.000", "Z Fighter", null),
            Item(2, "Vegeta", "Saiyan", "54.000.000", "Z Fighter", null),
            Item(3, "Piccolo", "Namekian", "2.000.000", "Z Fighter", null),
            Item(4, "Bulma", "Human", "0", "Z Fighter", null),
            Item(5, "Freezer", "Frieza Race", "530.000", "Army of Frieza", null)
        )
}
