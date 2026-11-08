package com.rojas.dragonball.data.remote.dto

import kotlinx.serialization.Serializable

// La lista de la API viene envuelta: { "items": [ ... ], "meta": {...}, "links": {...} }
@Serializable
data class RespuestaListaDto(
    val items: List<ItemDto>
)

// Un personaje tal como lo responde la API (los nombres estan en ingles)
@Serializable
data class ItemDto(
    val id: Int,
    val name: String,
    val ki: String = "",
    val race: String = "",
    val affiliation: String = "",
    val description: String = "",
    val image: String? = null,
    // Solo vienen en el endpoint de detalle
    val originPlanet: PlanetaDto? = null,
    val transformations: List<TransformacionDto> = emptyList()
)

@Serializable
data class PlanetaDto(
    val name: String = ""
)

@Serializable
data class TransformacionDto(
    val name: String = ""
)
