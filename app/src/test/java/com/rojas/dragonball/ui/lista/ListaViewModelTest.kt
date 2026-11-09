package com.rojas.dragonball.ui.lista

import com.rojas.dragonball.domain.model.Item
import com.rojas.dragonball.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    // Personajes de prueba
    private val goku = Item(1, "Goku", "Saiyan", "60.000.000", "Z Fighter", null)
    private val vegeta = Item(2, "Vegeta", "Saiyan", "54.000.000", "Z Fighter", null)
    private val piccolo = Item(3, "Piccolo", "Namekian", "2.000.000", "Z Fighter", null)

    // El ViewModel usa Dispatchers.Main, que no existe en las pruebas; se reemplaza
    @Before
    fun preparar() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun conInternet_muestraLaListaYNotifica() {
        val repo = RepositorioFalso(datosApi = listOf(goku, vegeta, piccolo))
        val notificador = NotificadorFalso()

        val vm = ListaViewModel(repo, notificador)

        val estado = vm.estado.value
        assertTrue(estado is UiState.Exito)
        assertEquals(3, (estado as UiState.Exito).datos.size)
        assertEquals(1, notificador.veces)
        assertEquals("Se guardaron 3 elementos", notificador.ultimoTexto)
    }

    @Test
    fun sinInternetYSinDatosGuardados_muestraError() {
        val repo = RepositorioFalso(datosApi = null)
        val notificador = NotificadorFalso()

        val vm = ListaViewModel(repo, notificador)

        val estado = vm.estado.value
        assertTrue(estado is UiState.Error)
        assertEquals("No hay conexión a internet", (estado as UiState.Error).mensaje)
        assertEquals(0, notificador.veces)
    }

    @Test
    fun sinInternetConDatosGuardados_muestraLoGuardadoConAviso() {
        val repo = RepositorioFalso(datosApi = null, datosGuardados = listOf(goku, vegeta))
        val notificador = NotificadorFalso()

        val vm = ListaViewModel(repo, notificador)

        val estado = vm.estado.value
        assertTrue(estado is UiState.Exito)
        assertEquals(2, (estado as UiState.Exito).datos.size)
        assertEquals("Sin conexión: mostrando datos guardados", vm.aviso.value)
        assertEquals(0, notificador.veces)
    }
}
