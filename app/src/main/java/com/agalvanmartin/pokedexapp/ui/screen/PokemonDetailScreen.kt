package com.agalvanmartin.pokedexapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.agalvanmartin.pokedexapp.data.repositories.ApiClient
import com.agalvanmartin.pokedexapp.data.model.PokemonDetail

@Composable
fun PokemonDetailScreen(pokemonId: Int) {
    var pokemonDetail by remember { mutableStateOf<PokemonDetail?>(null) }

    LaunchedEffect(pokemonId) {
        pokemonDetail = ApiClient.apiService.getPokemonDetails(pokemonId)
    }

    pokemonDetail?.let {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(model = "https://pokeapi.co/api/v2/pokemon/$pokemonId/", contentDescription = null)
            Text(text = "Peso: ${it.weight} - Altura: ${it.height}")
        }
    } ?: CircularProgressIndicator()
}
