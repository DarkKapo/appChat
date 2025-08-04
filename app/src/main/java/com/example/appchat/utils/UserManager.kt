package com.example.appchat.utils

import android.content.Context
import android.content.SharedPreferences

object UserManager {
    private const val PREFS_NAME = "usuario_prefs"
    private const val KEY_NOMBRE = "nombre_usuario"

    fun guardarNombre(context: Context, nombre: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_NOMBRE, nombre).apply()
    }

    fun obtenerNombre(context: Context): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_NOMBRE, null)
    }

    fun tieneNombreGuardado(context: Context): Boolean {
        return obtenerNombre(context) != null
    }
}