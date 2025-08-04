package com.example.appchat.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat.databinding.ActivityNombreUsuarioBinding
import com.example.appchat.presentation.ui.salas.SalasActivity
import com.example.appchat.utils.UserManager

class NombreUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNombreUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNombreUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGuardarNombre.setOnClickListener {
            val nombre = binding.etNombreUsuario.text.toString().trim()
            if (nombre.isNotEmpty()) {
                UserManager.guardarNombre(this, nombre)
                startActivity(Intent(this, SalasActivity::class.java))
                finish()
            }
        }
    }
}