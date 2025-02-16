package com.agalvanmartin.pokedexapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.agalvanmartin.pokedexapp.ui.navigation.APPnavigation
import com.agalvanmartin.pokedexapp.ui.theme.MyAppTheme
import com.agalvanmartin.pokedexapp.data.repositories.AuthManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                val navController = rememberNavController()
                val authManager = AuthManager(this)  // Instancia de AuthManager
                APPnavigation(auth = authManager)
            }
        }
    }
}
