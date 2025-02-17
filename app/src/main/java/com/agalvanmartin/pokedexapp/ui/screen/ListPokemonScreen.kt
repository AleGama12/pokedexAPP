package com.agalvanmartin.pokedexapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import com.agalvanmartin.pokedexapp.ui.screen.Pokemon

@Composable
fun ListPokemonScreen(navController: NavController) {
    var pokemonList by remember { mutableStateOf<List<Pokemon>>(emptyList()) }

    // Función para cargar los Pokémon desde Firestore
    fun loadPokemons() {
        val db = FirebaseFirestore.getInstance()

        // Obtener todos los Pokémon de la colección "pokemons"
        db.collection("pokemons")
            .get()
            .addOnSuccessListener { result ->
                // Convertir los documentos a objetos Pokémon
                val pokemons = result.documents.mapNotNull { document ->
                    document.toObject(Pokemon::class.java)
                }
                // Filtrar los Pokémon con datos completos
                pokemonList = pokemons.filter { it.name.isNotEmpty() && it.type.isNotEmpty() && it.abilities.isNotEmpty() }
            }
            .addOnFailureListener {
                Toast.makeText(navController.context, "Error al cargar los Pokémon.", Toast.LENGTH_SHORT).show()
            }
    }

    // Llamamos a la función para cargar los Pokémon cuando la pantalla se compone
    LaunchedEffect(Unit) {
        loadPokemons()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Flecha para regresar
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.Start)) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Listado de Pokémon", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar los Pokémon en una lista
        LazyColumn {
            items(pokemonList) { pokemon ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .shadow(5.dp, shape = MaterialTheme.shapes.medium), // Sombra para dar profundidad
                    shape = MaterialTheme.shapes.medium, // Bordes redondeados
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE0F7FA) // Color de fondo del card
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(text = "ID: ${pokemon.id}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Nombre: ${pokemon.name}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Tipo: ${pokemon.type}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Habilidades: ${pokemon.abilities}", style = MaterialTheme.typography.bodyMedium)

                        }
                    }
                }
            }
        }
    }
}
