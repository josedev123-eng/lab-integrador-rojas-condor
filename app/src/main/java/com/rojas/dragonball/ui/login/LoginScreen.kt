package com.rojas.dragonball.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rojas.dragonball.domain.model.Sesion
import com.rojas.dragonball.ui.UiState
import com.rojas.dragonball.ui.components.Cargando

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onIngresar: (Sesion) -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    // Cuando hay sesion se pasa a la lista
    LaunchedEffect(estado) {
        val e = estado
        if (e is UiState.Exito && e.datos != null) onIngresar(e.datos)
    }

    // Surface pinta el fondo del tema (claro u oscuro) para que el texto se lea bien
    Surface(modifier = Modifier.fillMaxSize()) {
        // Mientras revisa si ya habia sesion abierta
        if (estado is UiState.Cargando && correo.isEmpty()) {
            Cargando()
        } else {
            FormularioLogin(
                correo = correo,
                clave = clave,
                estado = estado,
                onCorreoChange = { correo = it },
                onClaveChange = { clave = it },
                onIngresar = { viewModel.iniciarSesion(correo, clave) }
            )
        }
    }
}

@Composable
fun FormularioLogin(
    correo: String,
    clave: String,
    estado: UiState<Sesion?>,
    onCorreoChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onIngresar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Dragon Ball",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text("Inicia sesión para continuar", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = onCorreoChange,
            label = { Text("Correo") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = clave,
            onValueChange = onClaveChange,
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        val e = estado
        if (e is UiState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(e.mensaje, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onIngresar,
            enabled = estado !is UiState.Cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (estado is UiState.Cargando) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Ingresar")
            }
        }
    }
}
