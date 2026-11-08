package com.rojas.dragonball.data.remote

import com.rojas.dragonball.data.remote.dto.ItemDto
import com.rojas.dragonball.data.remote.dto.RespuestaListaDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Endpoint 1: lista de personajes
    @GET("characters")
    suspend fun listar(@Query("limit") limite: Int = 58): RespuestaListaDto

    // Endpoint 2: detalle de un personaje (trae planeta y transformaciones)
    @GET("characters/{id}")
    suspend fun detalle(@Path("id") id: Int): ItemDto
}
