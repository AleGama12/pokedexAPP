package com.agalvanmartin.pokedexapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.agalvanmartin.pokedexapp.ui.navigation.APPnavigation
import com.agalvanmartin.pokedexapp.data.repositories.AuthManager
import com.agalvanmartin.pokedexapp.ui.theme.MyAppTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)

        setContent {
            MyAppTheme {
                val navController = rememberNavController()  // Controlador de navegación
                val authManager = AuthManager(this)  // Instancia de AuthManager para manejar la autenticación
                APPnavigation(auth = authManager)  // Llama a la función de navegación con el AuthManager
            }
        }
    }
}
