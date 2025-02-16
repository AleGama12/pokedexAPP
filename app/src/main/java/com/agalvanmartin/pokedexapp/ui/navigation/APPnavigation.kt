package com.agalvanmartin.pokedexapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.agalvanmartin.pokedexapp.data.repositories.AuthManager
import com.agalvanmartin.pokedexapp.ui.screen.ForgotPasswordScreen
import com.agalvanmartin.pokedexapp.ui.screen.LoginScreen
import com.agalvanmartin.pokedexapp.ui.screen.MainScreen
import com.agalvanmartin.pokedexapp.ui.screen.RegisterScreen

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
            RegisterScreen(
                auth
            ) { navController.popBackStack() }
        }

        composable<Main> {
            MainScreen(
                auth
            ) {
                navController.navigate(Login) {
                    popUpTo(Main) { inclusive = true }
                }
            }
        }

        composable <ForgotPassword> {
            ForgotPasswordScreen(
                auth
            ) { navController.navigate(Login) {
                popUpTo(Login){ inclusive = true }
            } }
        }
    }
}