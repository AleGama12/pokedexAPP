package com.agalvanmartin.pokedexapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agalvanmartin.pokedexapp.data.repositories.ApiClient
import com.agalvanmartin.pokedexapp.data.model.Pokemon

@Composable
fun MainScreen() {
    var pokemonList by remember { mutableStateOf<List<Pokemon>>(emptyList()) }

    LaunchedEffect(Unit) {
        pokemonList = ApiClient.apiService.getPokemonList().results
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(pokemonList.size) { index ->
            Text(
                text = pokemonList[index].name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { /* Acción al hacer clic en un Pokémon */ }
            )
        }
    }
}
