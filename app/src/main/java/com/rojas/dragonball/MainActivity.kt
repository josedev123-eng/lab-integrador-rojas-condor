package com.rojas.dragonball

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rojas.dragonball.ui.navigation.AppNav
import com.rojas.dragonball.ui.theme.DragonBallTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val contenedor = AppContainer(applicationContext)
        setContent {
            DragonBallTheme {
                AppNav(contenedor)
            }
        }
    }
}
