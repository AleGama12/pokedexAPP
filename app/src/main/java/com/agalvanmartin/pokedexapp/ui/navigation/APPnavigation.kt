package com.agalvanmartin.pokedexapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.agalvanmartin.pokedexapp.data.repositories.AuthManager
import com.agalvanmartin.pokedexapp.ui.screen.*

@Composable
fun APPnavigation(auth: AuthManager) {
    val navController = rememberNavController()  // Controlador de navegación
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                auth,
                { navController.navigate("signUp") },
                {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                { navController.navigate("forgotPassword") }
            )
        }

        composable("signUp") {
            RegisterScreen(auth) { navController.popBackStack() }
        }

        composable("main") {
            MainScreen(
                navController,
                navigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("forgotPassword") {
            ForgotPasswordScreen(auth) {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        // Pantallas para agregar, modificar, eliminar y listar Pokémon
        composable("addPokemonScreen") {
            AddPokemonScreen(navController)
        }

        composable("modifyPokemonScreen") {
            ModifyPokemonScreen(navController)
        }

        composable("deletePokemonScreen") {
            DeletePokemonScreen(navController)
        }

        composable("listPokemonScreen") {
            ListPokemonScreen(navController)
        }

        // Pantallas para agregar, modificar, eliminar y listar Estadios
        composable("addStadiumScreen") {
            AddStadiumScreen(navController)
        }

        // Ruta para modificar estadio con stadiumId como parámetro
        composable("modifyStadiumScreen/{stadiumId}") {
            ModifyStadiumScreen(navController)
        }

        // Ruta para eliminar estadio con stadiumId como parámetro
        composable("deleteStadiumScreen/{stadiumId}") {
            DeleteStadiumScreen(navController)
        }

        composable("listStadiumScreen") {
            ListStadiumScreen(navController)
        }
    }
}
