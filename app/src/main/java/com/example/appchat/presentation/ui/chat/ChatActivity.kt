package com.example.appchat.presentation.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchat.databinding.ActivityChatBinding
import com.example.appchat.data.database.AppDatabase
import com.example.appchat.data.database.MensajeEntity
import com.example.appchat.data.repository.MensajeRepository
import com.example.appchat.presentation.adapter.ChatAdapter
import com.example.appchat.presentation.viewmodel.ChatViewModel
import com.example.appchat.presentation.viewmodel.ChatViewModelFactory
import com.example.appchat.utils.NetworkUtils

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: ChatAdapter
    private val mensajes = mutableListOf<MensajeEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val salaId = intent.getStringExtra("salaId") ?: "default"

        // Crear repositorio y ViewModel
        val repo = MensajeRepository(
            AppDatabase.getInstance(this).mensajeDao()
        )
        val factory = ChatViewModelFactory(salaId, repo)
        viewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]

        // Configurar RecyclerView
        adapter = ChatAdapter(mensajes)
        binding.recyclerMensajes.layoutManager = LinearLayoutManager(this)
        binding.recyclerMensajes.adapter = adapter

        // Observar cambios en los mensajes
        viewModel.mensajes.observe(this) { lista ->
            mensajes.clear()
            mensajes.addAll(lista)
            adapter.notifyDataSetChanged()
            binding.recyclerMensajes.scrollToPosition(mensajes.size - 1)
        }

        // Iniciar WebSocket y cargar historial
        viewModel.iniciarWebSocket()

        // Enviar mensaje
        binding.btnEnviar.setOnClickListener {
            val texto = binding.etMensaje.text.toString()
            if (texto.isNotBlank()) {
                binding.etMensaje.text.clear()
                val conectado = NetworkUtils.estaConectado(this)
                viewModel.enviarMensaje(texto, conectado)
            }
        }
    }
}
