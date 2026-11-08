package com.rojas.dragonball.ui.detalle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.rojas.dragonball.domain.model.Item
import com.rojas.dragonball.ui.UiState
import com.rojas.dragonball.ui.components.AvisoSinConexion
import com.rojas.dragonball.ui.components.Cargando
import com.rojas.dragonball.ui.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    viewModel: DetalleViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del personaje") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            aviso?.let {
                AvisoSinConexion(texto = it, onReintentar = { viewModel.cargar() })
            }
            when (val e = estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(mensaje = e.mensaje, onReintentar = { viewModel.cargar() })
                is UiState.Exito -> ContenidoDetalle(item = e.datos, sinConexion = aviso != null)
            }
        }
    }
}

@Composable
fun ContenidoDetalle(item: Item, sinConexion: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AsyncImage(
            model = item.imagenUrl,
            contentDescription = item.nombre,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(item.nombre, style = MaterialTheme.typography.headlineMedium)
        Text("${item.raza} · ${item.afiliacion}", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Ki: ${item.ki}")

        // Sin internet solo se tienen los datos basicos guardados en Room
        if (sinConexion) return@Column

        Text("Planeta de origen: ${item.planeta.ifEmpty { "Desconocido" }}")

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(12.dp))

        Text("Descripción", style = MaterialTheme.typography.titleMedium)
        Text(item.descripcion, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(12.dp))
        Text("Transformaciones", style = MaterialTheme.typography.titleMedium)
        if (item.transformaciones.isEmpty()) {
            Text("No tiene transformaciones")
        } else {
            item.transformaciones.forEach { Text("• $it") }
        }
    }
}
