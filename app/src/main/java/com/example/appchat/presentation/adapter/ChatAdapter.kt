package com.example.appchat.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.R
import com.example.appchat.data.database.MensajeEntity
import com.example.appchat.databinding.ItemMensajeBinding

class ChatAdapter(private val mensajes: List<MensajeEntity>) :
    RecyclerView.Adapter<ChatAdapter.MensajeViewHolder>() {

    inner class MensajeViewHolder(val binding: ItemMensajeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val binding = ItemMensajeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MensajeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        val mensaje = mensajes[position]
        val estado = when (mensaje.estado) {
            "PENDIENTE" -> " ⌛"
            "ENVIADO" -> " ✓"
            "RECIBIDO" -> " 📥"
            else -> ""
        }

        holder.binding.tvMensaje.text = "${mensaje.contenido}$estado"

        val layoutParams = holder.binding.layoutMensaje.layoutParams as FrameLayout.LayoutParams

        if (mensaje.remitente == "Yo") {
            // Alineado a la derecha e izquierda con sus colores respectivos
            layoutParams.gravity = android.view.Gravity.END
            holder.binding.layoutMensaje.setBackgroundResource(R.drawable.bg_mensaje_yo)
        } else {
            layoutParams.gravity = android.view.Gravity.START
            holder.binding.layoutMensaje.setBackgroundResource(R.drawable.bg_mensaje_otros)
        }

        holder.binding.layoutMensaje.layoutParams = layoutParams
    }


    override fun getItemCount(): Int = mensajes.size
}