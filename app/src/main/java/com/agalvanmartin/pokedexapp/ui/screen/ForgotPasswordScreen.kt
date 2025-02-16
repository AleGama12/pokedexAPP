package com.agalvanmartin.pokedexapp.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.agalvanmartin.pokedexapp.data.repositories.AuthManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(navController: AuthManager, function: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver al Login", tint = LightBlue)
        }

        Text(
            text = "Olvidó su contraseña",
            fontSize = 24.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(50.dp))

        OutlinedTextField(
            label = { Text(text = "Correo electrónico") },
            value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                scope.launch {
                    resetPassword(email, auth, context, navController)
                }
            },
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue, contentColor = Color.White)
        ) {
            Text(text = "Recuperar contraseña")
        }
    }
}

fun resetPassword(email: String, auth: FirebaseAuth, context: Context, navController: NavController) {
    if (email.isNotEmpty()) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Se ha enviado un correo para restablecer la contraseña",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("login")
                } else {
                    Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    } else {
        Toast.makeText(context, "Ingrese un correo válido", Toast.LENGTH_SHORT).show()
    }
}
