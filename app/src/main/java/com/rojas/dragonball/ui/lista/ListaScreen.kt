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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.rojas.dragonball.domain.model.Item
import com.rojas.dragonball.ui.UiState
import com.rojas.dragonball.ui.components.Cargando
import com.rojas.dragonball.ui.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    onItemClick: (Int) -> Unit,
    viewModel: ListaViewModel = viewModel()
) {
    // La pantalla solo observa el estado del ViewModel
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Personajes de Dragon Ball") }) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val e = estado) {
                is UiState.Cargando -> Cargando()
                is UiState.Error -> ErrorView(mensaje = e.mensaje, onReintentar = { })
                is UiState.Exito -> {
                    if (e.datos.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay personajes para mostrar")
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(e.datos) { item ->
                                ItemCard(item = item, onClick = { onItemClick(item.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCard(item: Item, onClick: () -> Unit) {
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
            Column {
                Text(item.nombre, style = MaterialTheme.typography.titleMedium)
                Text("${item.raza} · Ki: ${item.ki}", style = MaterialTheme.typography.bodyMedium)
                Text(item.afiliacion, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
