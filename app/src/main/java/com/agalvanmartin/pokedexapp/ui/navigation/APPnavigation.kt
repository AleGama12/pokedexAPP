package com.agalvanmartin.pokedexapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.agalvanmartin.pokedexapp.data.repositories.AuthManager
import com.agalvanmartin.pokedexapp.ui.screen.*

@Composable
fun APPnavigation(auth: AuthManager) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            LoginScreen(
                auth,
                { navController.navigate(SignUp) },
                {
                    navController.navigate(Main) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                { navController.navigate(ForgotPassword) }
            )
        }

        composable<SignUp> {
            RegisterScreen(auth) { navController.popBackStack() }
        }

        composable<Main> {
            MainScreen(navController, navigateToLogin = {
                navController.navigate(Login) {
                    popUpTo(Login) { inclusive = true }
                }
            })
        }

        composable<ForgotPassword> {
            ForgotPasswordScreen(auth) {
                navController.navigate(Login) {
                    popUpTo(Login) { inclusive = true }
                }
            }
        }

        composable(
            route = "pokemon_detail/{id}/{name}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 1
            val name = backStackEntry.arguments?.getString("name") ?: "Unknown"

            PokemonDetailScreen(navController, id, name)
        }


    }
}
