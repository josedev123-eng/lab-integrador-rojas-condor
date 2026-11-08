package com.rojas.dragonball.data.mapper

import com.rojas.dragonball.data.local.ItemEntity
import com.rojas.dragonball.data.remote.dto.ItemDto
import com.rojas.dragonball.domain.model.Item

// API -> Room. esFavorito se pasa aparte para no perder los favoritos al sincronizar
fun ItemDto.aEntity(esFavorito: Boolean): ItemEntity = ItemEntity(
    id = id,
    nombre = name,
    raza = race,
    ki = ki,
    afiliacion = affiliation,
    imagenUrl = image,
    esFavorito = esFavorito
)

// Room -> UI
fun ItemEntity.aModelo(): Item = Item(
    id = id,
    nombre = nombre,
    raza = raza,
    ki = ki,
    afiliacion = afiliacion,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito
)

// API -> UI (para el detalle, que trae planeta y transformaciones)
fun ItemDto.aModelo(): Item = Item(
    id = id,
    nombre = name,
    raza = race,
    ki = ki,
    afiliacion = affiliation,
    imagenUrl = image,
    descripcion = description,
    planeta = originPlanet?.name ?: "",
    transformaciones = transformations.map { it.name }
)
