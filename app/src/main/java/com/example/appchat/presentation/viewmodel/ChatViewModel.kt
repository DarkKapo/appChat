package com.example.appchat.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchat.data.database.MensajeEntity
import com.example.appchat.data.datasource.websocket.ChatWebSocketClient
import com.example.appchat.data.repository.MensajeRepository
import com.example.appchat.domain.model.MensajeEstado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel(
    private val salaId: String,
    private val repository: MensajeRepository
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
            remitente = "Yo",
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
}