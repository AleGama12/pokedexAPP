package com.agalvanmartin.pokedexapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.agalvanmartin.pokedexapp.ui.screen.LightBlue

@Composable
fun LoginScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla
        Text(text = "Pokedex", style = MaterialTheme.typography.headlineMedium, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de correo electrónico sin borde extra
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de contraseña sin borde extra y botón para mostrar/ocultar contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = "Mostrar/Ocultar contraseña")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (loginError != null) {
            Text(text = loginError!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
        }

        // Botón de inicio de sesión (LightBlue)
        Button(onClick = {
            isLoading = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    isLoading = false
                    if (task.isSuccessful) {
                        navController.navigate("pokemon_list") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        loginError = "Error: ${task.exception?.message}"
                    }
                }
        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = LightBlue, contentColor = Color.White)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Iniciar sesión")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de "Entrar como invitado" con el logo de ojo
        Button(
            onClick = {
                auth.signInAnonymously()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.navigate("pokemon_list") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            loginError = "Error: ${task.exception?.message}"
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
        ) {
            Icon(imageVector = Icons.Filled.Visibility, contentDescription = "Modo Invitado") // Ícono del ojo
            Spacer(modifier = Modifier.width(8.dp))
            Text("Entrar como invitado")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de "Iniciar sesión con Google" (Sin funcionalidad por ahora)
        Button(
            onClick = { /* TODO: Implementar autenticación con Google */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.Black)
        ) {
            Text("Iniciar sesión con Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de "Registrarse" alineado a la izquierda
        TextButton(onClick = { navController.navigate("register") }, modifier = Modifier.align(Alignment.Start)) {
            Text("¿No tienes cuenta? Regístrate aquí", color = LightBlue)
        }

        // Botón de "Restablecer contraseña" alineado a la izquierda (LightBlue)
        TextButton(onClick = { navController.navigate("forgot_password") }, modifier = Modifier.align(Alignment.Start)) {
            Text("¿Olvidaste tu contraseña?", color = LightBlue)
        }
    }
}
