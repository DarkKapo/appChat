package com.example.appchat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appchat.data.repository.MensajeRepository

class ChatViewModelFactory(
    private val salaId: String,
    private val repository: MensajeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(salaId, repository) as T
    }
}
