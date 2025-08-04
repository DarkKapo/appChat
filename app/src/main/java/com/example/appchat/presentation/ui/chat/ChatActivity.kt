package com.example.appchat.presentation.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchat.databinding.ActivityChatBinding
import com.example.appchat.data.database.AppDatabase
import com.example.appchat.data.database.MensajeEntity
import com.example.appchat.data.datasource.websocket.ChatWebSocketClient
import com.example.appchat.data.repository.MensajeRepository
import com.example.appchat.domain.model.MensajeEstado
import com.example.appchat.presentation.adapter.ChatAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var mensajeRepository: MensajeRepository
    private lateinit var adapter: ChatAdapter
    private lateinit var chatWebSocket: ChatWebSocketClient
    private val mensajes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val salaId = intent.getStringExtra("salaId") ?: "default"

        // Inicializar repo
        mensajeRepository = MensajeRepository(
            AppDatabase.getInstance(this).mensajeDao()
        )

        // Configurar recyclerView
        adapter = ChatAdapter(mensajes)
        binding.recyclerMensajes.layoutManager = LinearLayoutManager(this)
        binding.recyclerMensajes.adapter = adapter

        // Cargar historial desde Room
        CoroutineScope(Dispatchers.IO).launch {
            val historial = mensajeRepository.obtenerMensajesPorSala(salaId)
            mensajes.addAll(historial.map { "${it.remitente}: ${it.contenido}" })
            runOnUiThread { adapter.notifyDataSetChanged() }
        }

        // Iniciar websocket
        chatWebSocket = ChatWebSocketClient(salaId) { mensajeRecibido ->
            runOnUiThread {
                mensajes.add("Amigo: $mensajeRecibido")
                adapter.notifyItemInserted(mensajes.size - 1)
            }

            val recibido = MensajeEntity(
                salaId = salaId,
                contenido = mensajeRecibido,
                remitente = "Amigo",
                estado = MensajeEstado.RECIBIDO,
                timestamp = System.currentTimeMillis()
            )
            CoroutineScope(Dispatchers.IO).launch {
                mensajeRepository.agregarMensaje(recibido)
            }
        }

        chatWebSocket.conectar()

        // Envio de mensaje
        binding.btnEnviar.setOnClickListener {
            val texto = binding.etMensaje.text.toString()
            if (texto.isNotBlank()) {
                mensajes.add("Yo: $texto")
                adapter.notifyItemInserted(mensajes.size - 1)
                chatWebSocket.enviarMensaje(texto)
                binding.etMensaje.text.clear()

                val nuevo = MensajeEntity(
                    salaId = salaId,
                    contenido = texto,
                    remitente = "Yo",
                    estado = MensajeEstado.ENVIADO,
                    timestamp = System.currentTimeMillis()
                )
                CoroutineScope(Dispatchers.IO).launch {
                    mensajeRepository.agregarMensaje(nuevo)
                }
            }
        }
    }
}
