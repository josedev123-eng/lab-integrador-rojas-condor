package com.rojas.dragonball.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rojas.dragonball.AppContainer
import com.rojas.dragonball.data.auth.AuthRepository
import com.rojas.dragonball.domain.model.Sesion
import com.rojas.dragonball.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val auth: AuthRepository
) : ViewModel() {

    // Exito(null) = no hay sesion (se muestra el formulario)
    // Exito(sesion) = ya ingreso
    private val _estado = MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)
    val estado: StateFlow<UiState<Sesion?>> = _estado.asStateFlow()

    init {
        // Si ya habia iniciado sesion antes, entra directo
        viewModelScope.launch {
            _estado.value = UiState.Exito(auth.sesionActual())
        }
    }

    fun iniciarSesion(correo: String, clave: String) {
        // Valida campos vacios sin llamar a Firebase
        if (correo.isBlank() || clave.isBlank()) {
            _estado.value = UiState.Error("Ingresa tu correo y contraseña")
            return
        }
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            auth.iniciarSesion(correo.trim(), clave)
                .onSuccess { _estado.value = UiState.Exito(it) }
                .onFailure { _estado.value = UiState.Error(it.message ?: "No se pudo ingresar") }
        }
    }

    fun cerrarSesion() {
        auth.cerrarSesion()
        _estado.value = UiState.Exito(null)
    }
}

class LoginViewModelFactory(
    private val contenedor: AppContainer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(contenedor.authRepository) as T
    }
}
