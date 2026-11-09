package com.rojas.dragonball.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rojas.dragonball.ui.UiState
import com.rojas.dragonball.ui.components.Cargando
import com.rojas.dragonball.ui.components.coloresBarra

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = coloresBarra(),
                title = { Text("Panel Admin") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (val e = estado) {
                is UiState.Cargando -> Cargando(modifier = Modifier.height(160.dp))
                is UiState.Error -> Text(e.mensaje, color = MaterialTheme.colorScheme.error)
                is UiState.Exito -> {
                    // IntrinsicSize.Min hace que las dos tarjetas tengan la misma altura
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(IntrinsicSize.Min)
                    ) {
                        TarjetaTotal("Guardados", e.datos.guardados, Modifier.weight(1f).fillMaxHeight())
                        TarjetaTotal("Favoritos", e.datos.favoritos, Modifier.weight(1f).fillMaxHeight())
                    }
                }
            }

            mensaje?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModel.sincronizar() },
                enabled = estado !is UiState.Cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sincronizar con la API")
            }
            OutlinedButton(
                onClick = { viewModel.limpiarCache() },
                enabled = estado !is UiState.Cargando,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Limpiar caché")
            }
        }
    }
}

@Composable
fun TarjetaTotal(titulo: String, valor: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$valor", style = MaterialTheme.typography.displaySmall)
            Text(titulo, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
