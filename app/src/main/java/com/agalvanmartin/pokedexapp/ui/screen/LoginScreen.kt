package com.agalvanmartin.pokedexapp.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.agalvanmartin.pokedexapp.R
import com.agalvanmartin.pokedexapp.data.repositories.AuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
    auth: AuthManager,
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val googleSignLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        scope.launch {
            auth.signInWithGoogle(result) { success, message ->
                if (success) {
                    Toast.makeText(context, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                    onNavigateToHome()
                } else {
                    Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            scope.launch {
                signIn(email, password, context, auth, onNavigateToHome)
            }
        }) {
            Text("Iniciar Sesión")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            scope.launch {
                signUp(email, password, context, auth, onNavigateToHome)
            }
        }) {
            Text("Registrarse")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { auth.signInWithGoogle(googleSignLauncher) }) {
            Text("Iniciar con Google")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            scope.launch {
                signInAnonymously(auth, context, onNavigateToHome)
            }
        }) {
            Text("Ingresar como Invitado")
        }
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(onClick = { onNavigateToForgotPassword() }) {
            Text("¿Olvidaste tu contraseña?")
        }
        TextButton(onClick = { onNavigateToSignUp() }) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}

suspend fun signIn(email: String, password: String, context: Context, auth: AuthManager, onSuccess: () -> Unit) {
    val result = withContext(Dispatchers.IO) { auth.signInWithEmail(email, password) }
    if (result) {
        Toast.makeText(context, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
        onSuccess()
    } else {
        Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
    }
}

suspend fun signUp(email: String, password: String, context: Context, auth: AuthManager, onSuccess: () -> Unit) {
    val result = withContext(Dispatchers.IO) { auth.signUpWithEmail(email, password) }
    if (result) {
        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
        onSuccess()
    } else {
        Toast.makeText(context, "Error al registrar", Toast.LENGTH_SHORT).show()
    }
}

suspend fun signInAnonymously(auth: AuthManager, context: Context, onSuccess: () -> Unit) {
    val result = withContext(Dispatchers.IO) { auth.signInAnonymously() }
    if (result) {
        Toast.makeText(context, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
        onSuccess()
    } else {
        Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
    }
}
