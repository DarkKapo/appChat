package com.example.appchat.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.databinding.ItemMensajeBinding

class ChatAdapter(private val mensajes: List<String>) :
    RecyclerView.Adapter<ChatAdapter.MensajeViewHolder>() {

    inner class MensajeViewHolder(val binding: ItemMensajeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val binding = ItemMensajeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MensajeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        holder.binding.tvMensaje.text = mensajes[position]
    }

    override fun getItemCount(): Int = mensajes.size
}