package com.agalvanmartin.pokedexapp.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.agalvanmartin.pokedexapp.viewmodel.PokemonViewModel
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth

val LightBlue = Color(0xFF87CEFA) // Azul más suave

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: PokemonViewModel = viewModel(),
    navigateToLogin: () -> Unit
) {
    val pokemonList = viewModel.pokemonList.collectAsState().value
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    // Verificar si el usuario está autenticado
    LaunchedEffect(user) {
        if (user == null) {
            navigateToLogin()  // Si no hay usuario, redirigir al LoginScreen
        }
    }

    Surface(color = Color.White) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Listado de Pokémon",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Botón para cerrar sesión
            Button(
                onClick = {
                    auth.signOut()
                    navigateToLogin()  // Redirigir al login después de cerrar sesión
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
            ) {
                Text("Cerrar Sesión", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(pokemonList.size) { index ->
                    val pokemon = pokemonList[index]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val route = "pokemon_detail/${pokemon.id}/${pokemon.name}"
                                Log.d("Navigation", "Navigating to: $route")

                                // Asegurarse de que la ruta existe antes de navegar
                                navController.navigate(route)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally  // Alineación centrada
                    ) {
                        AsyncImage(
                            model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.id}.png",
                            contentDescription = "Imagen de ${pokemon.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre imagen y texto
                        Text(
                            text = pokemon.name,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
