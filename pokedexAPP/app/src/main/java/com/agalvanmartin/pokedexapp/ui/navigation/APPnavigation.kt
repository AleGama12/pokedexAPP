package com.agalvanmartin.pokedexapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.agalvanmartin.pokedexapp.ui.screen.*

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navController)
        }
        composable("pokemon_list") {
            MainScreen(navController)
        }
        composable("pokemon_detail/{pokemonId}/{pokemonName}") { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId")?.toIntOrNull()
            val pokemonName = backStackEntry.arguments?.getString("pokemonName")

            if (pokemonId != null && pokemonName != null) {
                PokemonDetailScreen(navController, pokemonId, pokemonName)
            }
        }
    }
}
