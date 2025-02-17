package com.agalvanmartin.pokedexapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.agalvanmartin.pokedexapp.data.repositories.PokemonRepository
import kotlinx.coroutines.launch

data class Pokemon(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val abilities: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPokemonScreen(navController: NavController) {
    var pokemonId by remember { mutableStateOf("") }
    var pokemonName by remember { mutableStateOf("") }
    var pokemonType by remember { mutableStateOf("") }
    var pokemonAbilities by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val pokemonRepository = PokemonRepository()

    // Función para agregar el Pokémon
    fun addPokemonToFirestore() {
        val newPokemon = Pokemon(pokemonId, pokemonName, pokemonType, pokemonAbilities)
        scope.launch {
            try {
                pokemonRepository.addPokemon(newPokemon)
                Toast.makeText(navController.context, "Pokémon agregado exitosamente!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(navController.context, "Error al agregar el Pokémon.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Flecha para regresar
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.Start)) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Agregar Pokémon", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Cuadro de texto para ID del Pokémon con un color de borde personalizado
        TextField(
            value = pokemonId,
            onValueChange = { pokemonId = it },
            label = { Text("ID del Pokémon") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFE0F7FA), // Color de fondo de los campos
                focusedIndicatorColor = Color(0xFF00838F), // Color cuando está enfocado
                unfocusedIndicatorColor = Color(0xFF00838F), // Color cuando no está enfocado
                focusedLabelColor = Color(0xFF00838F), // Color de la etiqueta cuando está enfocado
                unfocusedLabelColor = Color(0xFF00838F), // Color de la etiqueta cuando no está enfocado
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cuadro de texto para Nombre del Pokémon
        TextField(
            value = pokemonName,
            onValueChange = { pokemonName = it },
            label = { Text("Nombre del Pokémon") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFE0F7FA),
                focusedIndicatorColor = Color(0xFF00838F),
                unfocusedIndicatorColor = Color(0xFF00838F),
                focusedLabelColor = Color(0xFF00838F),
                unfocusedLabelColor = Color(0xFF00838F),
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cuadro de texto para Tipo del Pokémon
        TextField(
            value = pokemonType,
            onValueChange = { pokemonType = it },
            label = { Text("Tipo del Pokémon") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFE0F7FA),
                focusedIndicatorColor = Color(0xFF00838F),
                unfocusedIndicatorColor = Color(0xFF00838F),
                focusedLabelColor = Color(0xFF00838F),
                unfocusedLabelColor = Color(0xFF00838F),
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cuadro de texto para Habilidades del Pokémon
        TextField(
            value = pokemonAbilities,
            onValueChange = { pokemonAbilities = it },
            label = { Text("Habilidades del Pokémon") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFE0F7FA),
                focusedIndicatorColor = Color(0xFF00838F),
                unfocusedIndicatorColor = Color(0xFF00838F),
                focusedLabelColor = Color(0xFF00838F),
                unfocusedLabelColor = Color(0xFF00838F),
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar el Pokémon con un color de fondo personalizado
        Button(
            onClick = { addPokemonToFirestore() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00838F), // Color de fondo del botón
                contentColor = Color.White // Color del texto del botón
            )
        ) {
            Text(text = "Agregar Pokémon")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddPokemonScreen() {
    AddPokemonScreen(navController = rememberNavController())
}
