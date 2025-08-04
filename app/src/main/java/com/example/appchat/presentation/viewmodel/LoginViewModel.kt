package com.example.appchat.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    fun login(usuario: String, contrasena: String) {
        // Simula login
        _loginResult.value = (usuario == "admin" && contrasena == "1234")
    }
}
