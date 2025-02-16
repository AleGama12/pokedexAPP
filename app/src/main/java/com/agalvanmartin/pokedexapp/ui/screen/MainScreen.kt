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
import androidx.navigation.NavController
import com.agalvanmartin.pokedexapp.data.repositories.AuthManager

val LightBlue = Color(0xFF87CEFA) // Azul más suave

@Composable
fun MainScreen(authManager: AuthManager, navController: NavController) {
    val user = authManager.getCurrentUser()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                authManager.signOut()
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Cerrar Sesión", color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Bienvenido, ${user?.email ?: "Invitado"}",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(10) { index ->  // Mostrando solo 10 Pokémon
                val pokemonId = index + 1
                val pokemonName = "Pokemon #$pokemonId"
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            navController.navigate("pokemon_detail/$pokemonId/$pokemonName")
                        },
                    colors = CardDefaults.cardColors(containerColor = LightBlue)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemonId}.png",
                            contentDescription = "Pokemon Image",
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = pokemonName, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
