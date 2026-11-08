package com.rojas.dragonball

import com.rojas.dragonball.data.remote.ApiService
import com.rojas.dragonball.data.remote.RetrofitClient
import com.rojas.dragonball.data.repository.ItemRepository
import com.rojas.dragonball.data.repository.ItemRepositoryImpl

// Crea las dependencias de la app en un solo lugar
class AppContainer {
    private val api: ApiService = RetrofitClient.api

    val itemRepository: ItemRepository = ItemRepositoryImpl(api)
}
