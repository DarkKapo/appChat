package com.example.appchat.data.repository

import androidx.room.*
import com.example.appchat.data.database.MensajeDao
import com.example.appchat.data.database.MensajeEntity

class MensajeRepository(private val dao: MensajeDao) {

    suspend fun agregarMensaje(mensaje: MensajeEntity) {
        dao.agregarMensaje(mensaje)
    }

    suspend fun obtenerMensajesPorSala(salaId: String): List<MensajeEntity> {
        return dao.obtenerMensajesPorSala(salaId)
    }

    suspend fun eliminarMensajesSala(salaId: String) {
        dao.eliminarMensajesSala(salaId)
    }
}
