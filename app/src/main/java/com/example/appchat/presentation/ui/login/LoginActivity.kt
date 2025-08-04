package com.example.appchat.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat.databinding.ActivityLoginBinding
import com.example.appchat.presentation.ui.salas.SalasActivity
import com.example.appchat.presentation.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val user = binding.etUsuario.text.toString()
            val pass = binding.etContrasena.text.toString()

            viewModel.login(user, pass)
        }

        viewModel.loginResult.observe(this) { success ->
            if (success) {
                // Ir a sala de chats
                startActivity(Intent(this, SalasActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
