package com.rojas.dragonball.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rojas.dragonball.AppContainer
import com.rojas.dragonball.ui.lista.ListaScreen
import com.rojas.dragonball.ui.lista.ListaViewModel
import com.rojas.dragonball.ui.lista.ListaViewModelFactory

@Composable
fun AppNav(contenedor: AppContainer) {
    val navController = rememberNavController()

    // Por ahora empieza en la lista; el login se agrega en H13
    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            val vm: ListaViewModel = viewModel(factory = ListaViewModelFactory(contenedor))
            ListaScreen(
                viewModel = vm,
                onItemClick = { id ->
                    // La pantalla de detalle se agrega en H7
                }
            )
        }
    }
}
