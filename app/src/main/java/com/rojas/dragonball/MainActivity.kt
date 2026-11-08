package com.rojas.dragonball

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.rojas.dragonball.ui.navigation.AppNav
import com.rojas.dragonball.ui.theme.DragonBallTheme

class MainActivity : ComponentActivity() {

    // Pide el permiso de notificaciones; si lo niega la app sigue funcionando
    private val pedirPermiso = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // El permiso POST_NOTIFICATIONS solo existe desde Android 13
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val contenedor = AppContainer(applicationContext)
        setContent {
            DragonBallTheme {
                AppNav(contenedor)
            }
        }
    }
}
