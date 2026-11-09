package com.rojas.dragonball.ui.lista

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun ListaScreen(
    viewModel: ListaViewModel,
    esAdmin: Boolean,
    onItemClick: (Int) -> Unit,
    onAbrirAdmin: () -> Unit,
    onVerFavoritos: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    // La pantalla solo observa el estado del ViewModel
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val aviso by viewModel.aviso.collectAsStateWithLifecycle()
    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val razas by viewModel.razas.collectAsStateWithLifecycle()
    val razaSeleccionada by viewModel.razaSeleccionada.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dragon Ball") },
                actions = {
                    // Solo el admin ve el acceso al panel
                    if (esAdmin) {
                        IconButton(onClick = onAbrirAdmin) {
                            Icon(Icons.Filled.AdminPanelSettings, contentDescription = "Panel Admin")
                        }
                    }
                    // Boton para ir a la pantalla Favoritos
                    IconButton(onClick = onVerFavoritos) {
                        Icon(Icons.Filled.Star, contentDescription = "Ver favoritos")
                    }
                    IconButton(onClick = onCerrarSesion) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Aviso de sin conexion (cuando hay datos guardados)
            aviso?.let {
                AvisoSinConexion(texto = it, onReintentar = { viewModel.refrescar() })
            }
            // Buscador por nombre
            OutlinedTextField(
                value = busqueda,
                onValueChange = { viewModel.buscar(it) },
                placeholder = { Text("Buscar personaje") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (busqueda.isNotEmpty()) {
                        IconButton(onClick = { viewModel.buscar("") }) {
                            Icon(Icons.Filled.Close, contentDescription = "Borrar búsqueda")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
            // Chips para filtrar por raza (se mueven de lado si no entran)
            if (razas.isNotEmpty()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = razaSeleccionada == null,
                            onClick = { viewModel.seleccionarRaza(null) },
                            label = { Text("Todos") }
                        )
                    }
                    items(razas) { raza ->
                        FilterChip(
                            selected = razaSeleccionada == raza,
                            onClick = { viewModel.seleccionarRaza(raza) },
                            label = { Text(raza) }
                        )
                    }
                }
            }
            when (val e = estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(mensaje = e.mensaje, onReintentar = { viewModel.refrescar() })
                is UiState.Exito -> {
                    if (e.datos.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                if (busqueda.isBlank() && razaSeleccionada == null) "No hay personajes para mostrar"
                                else "No se encontraron personajes con ese filtro"
                            )
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(e.datos) { item ->
                                ItemCard(
                                    item = item,
                                    onClick = { onItemClick(item.id) },
                                    onFavorito = { viewModel.alternarFavorito(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCard(item: Item, onClick: () -> Unit, onFavorito: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.nombre,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                Text("${item.raza} · Ki: ${item.ki}", style = MaterialTheme.typography.bodyMedium)
                Text(item.afiliacion, style = MaterialTheme.typography.bodySmall)
            }
            // Estrella llena si es favorito, vacia si no
            IconButton(onClick = onFavorito) {
                Icon(
                    imageVector = if (item.esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Favorito",
                    tint = if (item.esFavorito) Color(0xFFFFB300) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
