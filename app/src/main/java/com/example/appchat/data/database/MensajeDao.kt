package com.example.appchat.data.database

import androidx.room.*

@Dao
interface MensajeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarMensaje(mensaje: MensajeEntity)

    @Query("SELECT * FROM mensajes WHERE salaId = :salaId ORDER BY timestamp ASC")
    suspend fun obtenerMensajesPorSala(salaId: String): List<MensajeEntity>

    @Query("DELETE FROM mensajes WHERE salaId = :salaId")
    suspend fun eliminarMensajesSala(salaId: String)
}
