package com.agalvanmartin.pokedexapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPokemonScreen(navController: NavController) {
    var pokemonList by remember { mutableStateOf<List<Pokemon>>(emptyList()) }

    fun loadPokemons() {
        val db = FirebaseFirestore.getInstance()
        db.collection("pokemons")
            .get()
            .addOnSuccessListener { result ->
                val pokemons = result.documents.mapNotNull { document -> document.toObject(Pokemon::class.java) }
                pokemonList = pokemons.filter { it.name.isNotEmpty() }
            }
            .addOnFailureListener {
                Toast.makeText(navController.context, "Error al cargar los Pokémon.", Toast.LENGTH_SHORT).show()
            }
    }

    LaunchedEffect(Unit) { loadPokemons() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Pokémon", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF00838F))
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            items(pokemonList) { pokemon ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "ID: ${pokemon.id}", color = Color.Black)
                            Text(text = "Nombre: ${pokemon.name}", color = Color.Black)
                            Text(text = "Tipo: ${pokemon.type}", color = Color.Black)
                            Text(text = "Habilidades: ${pokemon.abilities}", color = Color.Black)
                        }
                        Row {
                            IconButton(onClick = { navController.navigate("modifyPokemonScreen") }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Black)
                            }
                            IconButton(onClick = { navController.navigate("deletePokemonScreen") }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}