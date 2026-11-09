package com.rojas.dragonball.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresClaros = lightColorScheme(
    primary = Naranja,
    onPrimary = Color.White,
    primaryContainer = NaranjaClaro,
    onPrimaryContainer = NaranjaOscuro,
    secondary = Azul,
    onSecondary = Color.White,
    secondaryContainer = AzulClaro,
    tertiary = Amarillo,
    background = FondoClaro,
    onBackground = TextoOscuro,
    surface = FondoClaro,
    onSurface = TextoOscuro,
    surfaceContainerHighest = TarjetaClara      // color de las tarjetas (Card)
)

private val ColoresOscuros = darkColorScheme(
    primary = NaranjaNoche,
    onPrimary = NaranjaOscuro,
    primaryContainer = NaranjaNocheContenedor,
    onPrimaryContainer = NaranjaClaro,
    secondary = AzulNoche,
    secondaryContainer = Color(0xFF0D3B78),
    tertiary = Amarillo,
    background = FondoOscuro,
    onBackground = TextoClaro,
    surface = FondoOscuro,
    onSurface = TextoClaro,
    surfaceContainerHighest = TarjetaOscura
)

@Composable
fun DragonBallTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Ya no se usan los colores dinamicos del celular: la app siempre tiene su propia paleta
    val colores = if (darkTheme) ColoresOscuros else ColoresClaros

    MaterialTheme(
        colorScheme = colores,
        typography = Typography,
        content = content
    )
}
