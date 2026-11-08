package com.rojas.dragonball.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rojas.dragonball.AppContainer
import com.rojas.dragonball.ui.detalle.DetalleScreen
import com.rojas.dragonball.ui.detalle.DetalleViewModel
import com.rojas.dragonball.ui.detalle.DetalleViewModelFactory
import com.rojas.dragonball.ui.favoritos.FavoritosScreen
import com.rojas.dragonball.ui.favoritos.FavoritosViewModel
import com.rojas.dragonball.ui.favoritos.FavoritosViewModelFactory
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
                onItemClick = { id -> navController.navigate("detalle/$id") },
                onVerFavoritos = { navController.navigate("favoritos") }
            )
        }
        composable("favoritos") {
            val vm: FavoritosViewModel = viewModel(factory = FavoritosViewModelFactory(contenedor))
            FavoritosScreen(
                viewModel = vm,
                onItemClick = { id -> navController.navigate("detalle/$id") },
                onVolver = { navController.popBackStack() }
            )
        }
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            val vm: DetalleViewModel = viewModel(factory = DetalleViewModelFactory(contenedor, id))
            DetalleScreen(
                viewModel = vm,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
