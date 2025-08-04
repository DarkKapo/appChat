package com.example.appchat.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.data.database.MensajeEntity
import com.example.appchat.databinding.ItemMensajeBinding
import com.example.appchat.domain.model.MensajeEstado

class ChatAdapter(
    private val mensajes: List<MensajeEntity>
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(val binding: ItemMensajeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemMensajeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val mensaje = mensajes[position]
        holder.binding.tvMensaje.text = formatearMensaje(mensaje)
    }

    override fun getItemCount(): Int = mensajes.size

    private fun formatearMensaje(m: MensajeEntity): String {
        val emoji = when (m.estado) {
            MensajeEstado.ENVIADO -> "✅"
            MensajeEstado.RECIBIDO -> "📬"
            MensajeEstado.VISTO -> "👁️"
            MensajeEstado.PENDIENTE -> "⏳"
            else -> "❓"
        }
        return "${m.remitente}: ${m.contenido} $emoji"
    }
}