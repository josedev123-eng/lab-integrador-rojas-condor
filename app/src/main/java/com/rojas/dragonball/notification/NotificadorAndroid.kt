package com.rojas.dragonball.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.rojas.dragonball.MainActivity
import com.rojas.dragonball.R

class NotificadorAndroid(
    private val context: Context
) : Notificador {

    private val idCanal = "sincronizacion"

    init {
        crearCanal()
    }

    // Desde Android 8 toda notificacion necesita un canal
    private fun crearCanal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                idCanal,
                "Sincronización",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Avisa cuando se guardan los personajes"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    // El permiso ya se revisa arriba, por eso se quita la advertencia
    @SuppressLint("MissingPermission")
    override fun mostrar(titulo: String, texto: String) {
        // Si el permiso esta negado no se muestra nada y la app sigue normal
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // Al tocar la notificacion se abre la app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(context, idCanal)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1, notificacion)
    }
}
