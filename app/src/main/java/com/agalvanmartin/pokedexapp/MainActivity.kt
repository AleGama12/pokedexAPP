package com.agalvanmartin.pokedexapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.agalvanmartin.pokedexapp.ui.navigation.AppNavigation
import com.agalvanmartin.pokedexapp.ui.theme.MyAppTheme
import com.agalvanmartin.pokedexapp.data.repositories.AuthManager

class MainActivity : ComponentActivity() {
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authManager = AuthManager(this)

        setContent {
            MyAppTheme {
                AppNavigation(authManager)
            }
        }
    }
}