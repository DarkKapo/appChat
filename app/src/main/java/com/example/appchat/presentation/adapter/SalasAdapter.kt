package com.example.appchat.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appchat.databinding.ItemSalaBinding
import com.example.appchat.domain.model.Sala

class SalasAdapter(
    private val salas: List<Sala>,
    private val onClick: (Sala) -> Unit
) : RecyclerView.Adapter<SalasAdapter.SalaViewHolder>() {

    inner class SalaViewHolder(val binding: ItemSalaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaViewHolder {
        val binding = ItemSalaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SalaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SalaViewHolder, position: Int) {
        val sala = salas[position]
        holder.binding.tvNombreSala.text = sala.nombre
        holder.binding.root.setOnClickListener { onClick(sala) }
    }

    override fun getItemCount(): Int = salas.size
}