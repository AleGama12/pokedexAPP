package com.agalvanmartin.pokedexapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.agalvanmartin.pokedexapp.data.repositories.AuthManager
import com.agalvanmartin.pokedexapp.ui.screen.LoginScreen
import com.agalvanmartin.pokedexapp.ui.screen.RegisterScreen
import com.agalvanmartin.pokedexapp.ui.screen.ForgotPasswordScreen
import com.agalvanmartin.pokedexapp.ui.screen.MainScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun AppNavigation(authManager: AuthManager) {
    val navController = rememberNavController()
    val isAuthenticated by rememberUpdatedState(authManager.getCurrentUser() != null)

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.navigate(Destinations.Main.route) {
                popUpTo(Destinations.Login.route) { inclusive = true }
            }
        } else {
            navController.navigate(Destinations.Login.route) {
                popUpTo(0)
            }
        }
    }

    NavHost(navController = navController, startDestination = if (isAuthenticated) Destinations.Main.route else Destinations.Login.route) {
        composable(Destinations.Login.route) { LoginScreen(navController) }
        composable(Destinations.Register.route) { RegisterScreen(navController) }
        composable(Destinations.ResetPassword.route) { ResetPasswordScreen(navController) }
        composable(Destinations.Main.route) { MainScreen() }
    }
}
