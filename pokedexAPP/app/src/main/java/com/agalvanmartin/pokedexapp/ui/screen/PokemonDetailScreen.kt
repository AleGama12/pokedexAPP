package com.agalvanmartin.pokedexapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.agalvanmartin.pokedexapp.data.repositories.ApiClient
import com.agalvanmartin.pokedexapp.data.model.PokemonDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.agalvanmartin.pokedexapp.ui.screen.LightBlue
import androidx.navigation.NavController

@Composable
fun PokemonDetailScreen(navController: NavController, pokemonId: Int, pokemonName: String) {
    val coroutineScope = rememberCoroutineScope()
    var details by remember { mutableStateOf<PokemonDetail?>(null) }

    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                details = ApiClient.getPokemonDetails(pokemonId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.Start)) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Volver", tint = LightBlue)
            }

            Text(
                text = "Detalles de $pokemonName",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png",
                contentDescription = "Image of $pokemonName",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            details?.let {
                Text(text = "ID: $pokemonId", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                Text(text = "Peso: ${it.weight} kg", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                Text(text = "Altura: ${it.height} m", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                Text(text = "Tipos: ${it.types.joinToString { type -> type.type.name }}", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                Text(
                    text = "Habilidades: ${it.abilities.joinToString { ability -> ability.ability.name }}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            } ?: Text(text = "Cargando detalles...", style = MaterialTheme.typography.bodyMedium, color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}
