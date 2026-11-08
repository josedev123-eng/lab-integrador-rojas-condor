package com.rojas.dragonball

import android.content.Context
import com.rojas.dragonball.data.auth.AuthRepository
import com.rojas.dragonball.data.auth.FirebaseAuthRepository
import com.rojas.dragonball.data.local.AppDatabase
import com.rojas.dragonball.data.remote.ApiService
import com.rojas.dragonball.data.remote.RetrofitClient
import com.rojas.dragonball.data.repository.ItemRepository
import com.rojas.dragonball.data.repository.ItemRepositoryImpl
import com.rojas.dragonball.notification.Notificador
import com.rojas.dragonball.notification.NotificadorAndroid

// Crea las dependencias de la app en un solo lugar
class AppContainer(context: Context) {
    private val api: ApiService = RetrofitClient.api
    private val baseDeDatos: AppDatabase = AppDatabase.obtener(context)

    val itemRepository: ItemRepository = ItemRepositoryImpl(api, baseDeDatos.itemDao())
    val authRepository: AuthRepository = FirebaseAuthRepository()
    val notificador: Notificador = NotificadorAndroid(context.applicationContext)
}
