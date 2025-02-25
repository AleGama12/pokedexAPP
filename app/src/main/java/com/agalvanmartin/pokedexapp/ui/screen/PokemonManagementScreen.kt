package com.agalvanmartin.pokedexapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

data class Pokemon(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val abilities: String = ""
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonManagementScreen(navController: NavController) {
    var pokemonList by remember { mutableStateOf<List<Pokemon>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var newPokemonId by remember { mutableStateOf("") }
    var newPokemonName by remember { mutableStateOf("") }
    var newPokemonType by remember { mutableStateOf("") }
    var newPokemonAbilities by remember { mutableStateOf("") }
    var selectedPokemon by remember { mutableStateOf<Pokemon?>(null) }

    fun loadPokemons() {
        val db = FirebaseFirestore.getInstance()
        db.collection("pokemons")
            .get()
            .addOnSuccessListener { result ->
                val pokemons = result.documents.mapNotNull { it.toObject(Pokemon::class.java) }
                pokemonList = pokemons.filter { it.name.isNotEmpty() }.sortedBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }
            }
            .addOnFailureListener {
                Toast.makeText(navController.context, "Error al cargar los Pokémon.", Toast.LENGTH_SHORT).show()
            }
    }


    fun addPokemon() {
        if (newPokemonId.isNotEmpty() && newPokemonName.isNotEmpty() && newPokemonType.isNotEmpty() && newPokemonAbilities.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            val newPokemon = hashMapOf(
                "id" to newPokemonId,
                "name" to newPokemonName,
                "type" to newPokemonType,
                "abilities" to newPokemonAbilities
            )
            db.collection("pokemons").add(newPokemon)
                .addOnSuccessListener {
                    Toast.makeText(navController.context, "Pokémon agregado.", Toast.LENGTH_SHORT).show()
                    loadPokemons()
                    showDialog = false
                }
                .addOnFailureListener {
                    Toast.makeText(navController.context, "Error al agregar Pokémon.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun updatePokemon() {
        selectedPokemon?.let { pokemon ->
            val db = FirebaseFirestore.getInstance()
            db.collection("pokemons").whereEqualTo("id", pokemon.id).get()
                .addOnSuccessListener { result ->
                    for (document in result.documents) {
                        db.collection("pokemons").document(document.id).update(
                            mapOf(
                                "name" to newPokemonName,
                                "type" to newPokemonType,
                                "abilities" to newPokemonAbilities
                            )
                        ).addOnSuccessListener {
                            Toast.makeText(navController.context, "Pokémon actualizado.", Toast.LENGTH_SHORT).show()
                            loadPokemons()
                            showEditDialog = false
                        }.addOnFailureListener {
                            Toast.makeText(navController.context, "Error al actualizar Pokémon.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    fun deletePokemon(pokemon: Pokemon) {
        val db = FirebaseFirestore.getInstance()
        db.collection("pokemons").whereEqualTo("id", pokemon.id).get()
            .addOnSuccessListener { result ->
                for (document in result.documents) {
                    db.collection("pokemons").document(document.id).delete()
                        .addOnSuccessListener {
                            Toast.makeText(navController.context, "Pokémon eliminado.", Toast.LENGTH_SHORT).show()
                            loadPokemons()
                            showDeleteDialog = false
                        }
                        .addOnFailureListener {
                            Toast.makeText(navController.context, "Error al eliminar Pokémon.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }

    LaunchedEffect(Unit) { loadPokemons() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Pokémon", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("main") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF00838F))
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                newPokemonId = ""
                newPokemonName = ""
                newPokemonType = ""
                newPokemonAbilities = ""
                showDialog = true
            }, containerColor = Color(0xFF00838F)) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Pokémon", tint = Color.White)
            }
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
                            IconButton(onClick = {
                                selectedPokemon = pokemon
                                newPokemonName = pokemon.name
                                newPokemonType = pokemon.type
                                newPokemonAbilities = pokemon.abilities
                                showEditDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Black)
                            }
                            IconButton(onClick = { selectedPokemon = pokemon; showDeleteDialog = true }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog && selectedPokemon != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Pokémon") },
            text = { Text("¿Seguro que deseas eliminar a ${selectedPokemon?.name}?") },
            confirmButton = {
                Button(onClick = { deletePokemon(selectedPokemon!!) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                    Text("Cancelar", color = Color.White)
                }
            }
        )
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Agregar Nuevo Pokémon", style = MaterialTheme.typography.titleLarge, color = Color(0xFF00838F))
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = newPokemonId, onValueChange = { newPokemonId = it }, label = { Text("ID") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = newPokemonName, onValueChange = { newPokemonName = it }, label = { Text("Nombre") })
                    OutlinedTextField(value = newPokemonType, onValueChange = { newPokemonType = it }, label = { Text("Tipo") })
                    OutlinedTextField(value = newPokemonAbilities, onValueChange = { newPokemonAbilities = it }, label = { Text("Habilidades") })
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = { addPokemon() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))) {
                            Text("Agregar", color = Color.White)
                        }
                        Button(onClick = { showDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                            Text("Cancelar", color = Color.White)
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        Dialog(onDismissRequest = { showEditDialog = false }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Editar Pokémon", style = MaterialTheme.typography.titleLarge, color = Color(0xFF00838F))
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = newPokemonName, onValueChange = { newPokemonName = it }, label = { Text("Nombre") })
                    OutlinedTextField(value = newPokemonType, onValueChange = { newPokemonType = it }, label = { Text("Tipo") })
                    OutlinedTextField(value = newPokemonAbilities, onValueChange = { newPokemonAbilities = it }, label = { Text("Habilidades") })
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = { updatePokemon() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))) {
                            Text("Actualizar", color = Color.White)
                        }
                        Button(onClick = { showEditDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
                            Text("Cancelar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
