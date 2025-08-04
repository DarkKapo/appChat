package com.example.appchat.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchat.data.database.MensajeEntity
import com.example.appchat.data.datasource.websocket.ChatWebSocketClient
import com.example.appchat.data.repository.MensajeRepository
import com.example.appchat.domain.model.MensajeEstado
import com.example.appchat.utils.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel(
    private val salaId: String,
    private val repository: MensajeRepository,
    private val context: Context
) : ViewModel() {

    private val _mensajes = MutableLiveData<List<MensajeEntity>>(emptyList())
    val mensajes: LiveData<List<MensajeEntity>> = _mensajes

    private lateinit var chatWebSocket: ChatWebSocketClient

    fun iniciarWebSocket(onRecibido: () -> Unit = {}) {
        chatWebSocket = ChatWebSocketClient(salaId) { texto ->
            val recibido = MensajeEntity(
                salaId = salaId,
                contenido = texto,
                remitente = "Amigo",
                estado = MensajeEstado.RECIBIDO,
                timestamp = System.currentTimeMillis()
            )

            viewModelScope.launch(Dispatchers.IO) {
                repository.agregarMensaje(recibido)
                cargarMensajes()
            }

            onRecibido()
        }

        chatWebSocket.conectar()
        reintentarPendientes()
        cargarMensajes()
    }

    fun enviarMensaje(contenido: String, estaConectado: Boolean) {
        val estado = if (estaConectado) MensajeEstado.ENVIADO else MensajeEstado.PENDIENTE

        val nuevo = MensajeEntity(
            salaId = salaId,
            contenido = contenido,
            remitente = UserManager.obtenerNombre(context) ?: "Yo",
            estado = estado,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch(Dispatchers.IO) {
            repository.agregarMensaje(nuevo)
            if (estado == MensajeEstado.ENVIADO) {
                chatWebSocket.enviarMensaje(contenido)
            }
            cargarMensajes()
        }
    }

    fun cargarMensajes() {
        viewModelScope.launch(Dispatchers.IO) {
            val lista = repository.obtenerMensajesPorSala(salaId)
            _mensajes.postValue(lista)
        }
    }

    fun reintentarPendientes() {
        viewModelScope.launch(Dispatchers.IO) {
            val pendientes = repository
                .obtenerMensajesPorSala(salaId)
                .filter { it.estado == MensajeEstado.PENDIENTE }

            for (m in pendientes) {
                chatWebSocket.enviarMensaje(m.contenido)
                val actualizado = m.copy(estado = MensajeEstado.ENVIADO)
                repository.agregarMensaje(actualizado)
            }

            cargarMensajes()
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatWebSocket.cerrar()
    }
    fun enviarImagen(base64: String) {
        val mensaje = MensajeEntity(
            salaId = salaId,
            contenido = "[imagen]:$base64",
            remitente = "Yo",
            estado = MensajeEstado.ENVIADO,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch(Dispatchers.IO) {
            chatWebSocket.enviarMensaje(mensaje.contenido)
            repository.agregarMensaje(mensaje)
            cargarMensajes()
        }
    }

    fun marcarMensajesComoVistos() {
        viewModelScope.launch {
            val recibidos = repository
                .obtenerMensajesPorSala(salaId)
                .filter { it.estado == MensajeEstado.RECIBIDO }

            for (m in recibidos) {
                val actualizado = m.copy(estado = MensajeEstado.VISTO)
                repository.agregarMensaje(actualizado)
            }

            // Recargar lista
            _mensajes.value = repository.obtenerMensajesPorSala(salaId)
        }
    }
}