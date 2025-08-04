package com.example.appchat.data.datasource.websocket

import okhttp3.*
import java.util.concurrent.TimeUnit

class ChatWebSocketClient(
    private val salaId: String,
    private val onMessageReceived: (String) -> Unit
) {
    private val client = OkHttpClient.Builder()
        .pingInterval(15, TimeUnit.SECONDS)
        .build()

    private var webSocket: WebSocket? = null

    fun conectar() {
        val request = Request.Builder()
            .url("wss://echo.websocket.org") // Para pruebas
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(ws: WebSocket, text: String) {
                onMessageReceived(text)
            }
        })
    }

    fun enviarMensaje(mensaje: String) {
        webSocket?.send(mensaje)
    }

    fun cerrar() {
        webSocket?.close(1000, "Usuario cerró el chat")
    }
}