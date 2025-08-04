package com.example.appchat.presentation.ui.salas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchat.databinding.ActivitySalasBinding
import com.example.appchat.domain.model.Sala
import com.example.appchat.presentation.adapter.SalasAdapter
import com.example.appchat.presentation.ui.chat.ChatActivity

class SalasActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalasBinding
    private lateinit var adapter: SalasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val salas = listOf(
            Sala("1", "General"),
            Sala("2", "Programación"),
            Sala("3", "Videojuegos")
        )

        adapter = SalasAdapter(salas) { sala ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("salaId", sala.id)
            intent.putExtra("salaNombre", sala.nombre)
            startActivity(intent)
        }

        binding.recyclerSalas.layoutManager = LinearLayoutManager(this)
        binding.recyclerSalas.adapter = adapter
    }
}
