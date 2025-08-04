package com.example.appchat.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mensajes")
data class MensajeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val salaId: String,
    val contenido: String,
    val remitente: String,
    val estado: String = "ENVIADO",
    val timestamp: Long
)
