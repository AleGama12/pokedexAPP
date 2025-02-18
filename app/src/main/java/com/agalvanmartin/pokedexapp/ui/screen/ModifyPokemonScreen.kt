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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyPokemonScreen(navController: NavController) {
    var pokemonId by remember { mutableStateOf("") }
    var pokemonName by remember { mutableStateOf("") }
    var pokemonType by remember { mutableStateOf("") }
    var pokemonAbilities by remember { mutableStateOf("") }
    var pokemonFound by remember { mutableStateOf(false) }

    fun searchPokemon() {
        val db = FirebaseFirestore.getInstance()

        if (pokemonId.isEmpty()) {
            Toast.makeText(navController.context, "Por favor ingresa un ID válido", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("pokemons")
            .document(pokemonId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val pokemon = document.toObject(Pokemon::class.java)
                    if (pokemon != null) {
                        pokemonName = pokemon.name
                        pokemonType = pokemon.type
                        pokemonAbilities = pokemon.abilities
                        pokemonFound = true
                    }
                } else {
                    // Si no se encuentra el Pokémon
                    pokemonFound = false
                    Toast.makeText(navController.context, "Pokémon no encontrado.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(navController.context, "Error al buscar el Pokémon.", Toast.LENGTH_SHORT).show()
            }
    }

    fun updatePokemonInFirestore() {
        val db = FirebaseFirestore.getInstance()

        if (pokemonId.isEmpty()) {
            Toast.makeText(navController.context, "Por favor ingresa un ID válido", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedPokemon = Pokemon(pokemonId, pokemonName, pokemonType, pokemonAbilities)

        db.collection("pokemons")
            .document(pokemonId)
            .set(updatedPokemon)
            .addOnSuccessListener {
                Toast.makeText(navController.context, "Pokémon actualizado exitosamente!", Toast.LENGTH_SHORT).show()
                navController.navigate("listPokemonScreen")
            }
            .addOnFailureListener {
                Toast.makeText(navController.context, "Error al actualizar el Pokémon.", Toast.LENGTH_SHORT).show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.Start)) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Modificar Pokémon", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = pokemonId,
            onValueChange = { pokemonId = it },
            label = { Text("ID del Pokémon") },
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

        Button(
            onClick = { searchPokemon() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00838F),
                contentColor = Color.White
            )
        ) {
            Text(text = "Buscar Pokémon")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (pokemonFound) {
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

            Button(
                onClick = { updatePokemonInFirestore() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00838F),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Guardar Cambios")
            }
        }
    }
}