package com.example.appchat.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appchat.data.repository.MensajeRepository

class ChatViewModelFactory(
    private val salaId: String,
    private val repository: MensajeRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(salaId, repository, context) as T
    }
}
