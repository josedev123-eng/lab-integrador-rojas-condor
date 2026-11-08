package com.rojas.dragonball.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rojas.dragonball.ui.lista.ListaScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    // Por ahora empieza en la lista; el login se agrega en H13
    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaScreen(
                onItemClick = { id ->
                    // La pantalla de detalle se agrega en H7
                }
            )
        }
    }
}
