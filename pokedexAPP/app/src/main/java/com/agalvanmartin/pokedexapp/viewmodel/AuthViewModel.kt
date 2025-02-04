package com.agalvanmartin.pokedexapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Iniciar sesión con email y contraseña.
     */
    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            onResult(false, "Email y contraseña no pueden estar vacíos.")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Inicio de sesión exitoso: ${task.result?.user?.email}")
                    onResult(true, null)
                } else {
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Log.e("AuthViewModel", "Error en inicio de sesión: $errorMessage")
                    onResult(false, errorMessage)
                }
            }
            .addOnFailureListener { exception ->
                val errorMessage = exception.message ?: "Error desconocido"
                Log.e("AuthViewModel", "Error en inicio de sesión: $errorMessage")
                onResult(false, errorMessage)
            }
    }

    /**
     * Registrar un nuevo usuario con email y contraseña.
     */
    fun registerUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            onResult(false, "Email y contraseña no pueden estar vacíos.")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AuthViewModel", "Registro exitoso: ${task.result?.user?.email}")
                    onResult(true, null)
                } else {
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Log.e("AuthViewModel", "Error en el registro: $errorMessage")
                    onResult(false, errorMessage)
                }
            }
            .addOnFailureListener { exception ->
                val errorMessage = exception.message ?: "Error desconocido"
                Log.e("AuthViewModel", "Error en el registro: $errorMessage")
                onResult(false, errorMessage)
            }
    }
}