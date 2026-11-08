package com.rojas.dragonball.data.mapper

import com.rojas.dragonball.data.remote.dto.ItemDto
import com.rojas.dragonball.domain.model.Item

// Dto (API) -> Item (UI). En H8 se agrega Dto -> Entity -> Item
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
