package com.rojas.dragonball.ui.lista

import com.rojas.dragonball.notification.Notificador

// Notificador falso: no muestra nada, solo anota si se le pidio notificar
class NotificadorFalso : Notificador {
    var veces = 0
    var ultimoTexto: String? = null

    override fun mostrar(titulo: String, texto: String) {
        veces++
        ultimoTexto = texto
    }
}
