package com.rojas.dragonball.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rojas.dragonball.AppContainer
import com.rojas.dragonball.domain.model.Rol
import com.rojas.dragonball.ui.UiState
import com.rojas.dragonball.ui.admin.AdminScreen
import com.rojas.dragonball.ui.admin.AdminViewModel
import com.rojas.dragonball.ui.admin.AdminViewModelFactory
import com.rojas.dragonball.ui.detalle.DetalleScreen
import com.rojas.dragonball.ui.detalle.DetalleViewModel
import com.rojas.dragonball.ui.detalle.DetalleViewModelFactory
import com.rojas.dragonball.ui.favoritos.FavoritosScreen
import com.rojas.dragonball.ui.favoritos.FavoritosViewModel
import com.rojas.dragonball.ui.favoritos.FavoritosViewModelFactory
import com.rojas.dragonball.ui.lista.ListaScreen
import com.rojas.dragonball.ui.lista.ListaViewModel
import com.rojas.dragonball.ui.lista.ListaViewModelFactory
import com.rojas.dragonball.ui.login.LoginScreen
import com.rojas.dragonball.ui.login.LoginViewModel
import com.rojas.dragonball.ui.login.LoginViewModelFactory

@Composable
fun AppNav(contenedor: AppContainer) {
    val navController = rememberNavController()

    // Se crea fuera del NavHost para que la sesion se comparta en toda la app
    val loginVm: LoginViewModel = viewModel(factory = LoginViewModelFactory(contenedor))
    val estadoLogin by loginVm.estado.collectAsStateWithLifecycle()

    // El rol viene de Firestore (se leyo al iniciar sesion)
    val sesion = (estadoLogin as? UiState.Exito)?.datos
    val esAdmin = sesion?.rol == Rol.ADMIN

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = loginVm,
                onIngresar = {
                    // Al ingresar se borra el login del historial para no volver con "atras"
                    navController.navigate("lista") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("lista") {
            val vm: ListaViewModel = viewModel(factory = ListaViewModelFactory(contenedor))
            ListaScreen(
                viewModel = vm,
                esAdmin = esAdmin,
                onItemClick = { id -> navController.navigate("detalle/$id") },
                onAbrirAdmin = { navController.navigate("admin") },
                onVerFavoritos = { navController.navigate("favoritos") },
                onCerrarSesion = {
                    loginVm.cerrarSesion()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable("admin") {
            // Si alguien que no es admin llega aqui, se regresa
            if (!esAdmin) {
                LaunchedEffect(Unit) { navController.popBackStack() }
                return@composable
            }
            val vm: AdminViewModel = viewModel(factory = AdminViewModelFactory(contenedor))
            AdminScreen(
                viewModel = vm,
                onVolver = { navController.popBackStack() }
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
