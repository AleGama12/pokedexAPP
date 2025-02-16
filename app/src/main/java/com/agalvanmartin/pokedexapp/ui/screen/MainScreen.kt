package com.agalvanmartin.pokedexapp.ui.screen

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
import coil.compose.AsyncImage
import com.agalvanmartin.pokedexapp.data.repositories.AuthManager
import com.google.firebase.auth.FirebaseAuth

val LightBlue = Color(0xFF87CEFA) // Azul más suave

@Composable
fun MainScreen(navController: AuthManager, viewModel: () -> Unit = viewModel()) {
    val pokemonList = viewModel.pokemonList.collectAsState().value

    Surface(color = Color.White) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(text = "Listado de Pokémon", style = MaterialTheme.typography.headlineMedium, color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(pokemonList.size) { index ->
                    val pokemon = pokemonList[index]
                    Column(
                        modifier = Modifier.fillMaxWidth().clickable { navController.navigate("pokemon_detail/${pokemon.id}/${pokemon.name}") },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.id}.png",
                            contentDescription = "Image of ${pokemon.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )
                        Text(
                            text = pokemon.name.capitalize(),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("pokemon_list") { inclusive = true }
                    }
                },
                modifier = Modifier.align(Alignment.Start),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue, contentColor = Color.White)
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}
