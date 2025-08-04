package com.example.appchat.presentation.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val salaId = intent.getStringExtra("salaId")
        val salaNombre = intent.getStringExtra("salaNombre")

        title = salaNombre ?: "Chat"
    }
}
