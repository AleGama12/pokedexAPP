package com.agalvanmartin.pokedexapp.ui.screen

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.agalvanmartin.pokedexapp.ui.components.SearchBar
import com.agalvanmartin.pokedexapp.ui.components.StatsHeader
import com.agalvanmartin.pokedexapp.ui.components.SortOrder
import com.agalvanmartin.pokedexapp.viewmodel.Pokemon
import com.agalvanmartin.pokedexapp.viewmodel.PokemonViewModel
import com.agalvanmartin.pokedexapp.viewmodel.base.ViewState
import kotlinx.coroutines.launch
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PokemonManagementScreen(navController: NavController, viewModel: PokemonViewModel = PokemonViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var currentSort by remember { mutableStateOf(SortOrder.ID_ASC) }
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var newPokemonId by remember { mutableStateOf("") }
    var newPokemonName by remember { mutableStateOf("") }
    var newPokemonType by remember { mutableStateOf("") }
    var newPokemonAbilities by remember { mutableStateOf("") }
    var selectedPokemon by remember { mutableStateOf<Pokemon?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
        when (val currentState = state) {
            is ViewState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ViewState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
                    item {
                        StatsHeader(
                            totalItems = currentState.data.pokemons.size,
                            currentSort = currentSort,
                            onSortChange = { currentSort = it },
                            containerColor = Color(0xFF00838F)
                        )
                        
                        SearchBar(
                            searchText = searchText,
                            onSearchTextChange = { searchText = it }
                        )
                    }
                    
                    val filteredPokemons = currentState.data.pokemons
                        .filter { it.name.contains(searchText, ignoreCase = true) }
                        .let { pokemons ->
                            when (currentSort) {
                                SortOrder.ID_ASC -> pokemons.sortedBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }
                                SortOrder.ID_DESC -> pokemons.sortedByDescending { it.id.toIntOrNull() ?: Int.MIN_VALUE }
                                SortOrder.NAME_ASC -> pokemons.sortedBy { it.name }
                                SortOrder.NAME_DESC -> pokemons.sortedByDescending { it.name }
                            }
                        }
                    
                    items(filteredPokemons, key = { it.id }) { pokemon ->
                        val interactionSource = remember { MutableInteractionSource() }
                        val isHovered by interactionSource.collectIsHoveredAsState()
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .animateItemPlacement()
                                .hoverable(interactionSource)
                                .animateContentSize(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = if (isHovered) 8.dp else 2.dp
                            )
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
            is ViewState.Error -> {
                Toast.makeText(context, "Error: ${currentState.error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    if (showDeleteDialog && selectedPokemon != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Pokémon") },
            text = { Text("¿Seguro que deseas eliminar a ${selectedPokemon?.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            selectedPokemon?.let { pokemon ->
                                viewModel.deletePokemon(pokemon).onSuccess {
                                    Toast.makeText(context, "Pokémon eliminado", Toast.LENGTH_SHORT).show()
                                    showDeleteDialog = false
                                }.onFailure {
                                    Toast.makeText(context, "Error al eliminar: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
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
                        Button(
                            onClick = {
                                scope.launch {
                                    if (newPokemonId.isNotEmpty() && newPokemonName.isNotEmpty() && 
                                        newPokemonType.isNotEmpty() && newPokemonAbilities.isNotEmpty()) {
                                        val pokemon = Pokemon(
                                            id = newPokemonId,
                                            name = newPokemonName,
                                            type = newPokemonType,
                                            abilities = newPokemonAbilities
                                        )
                                        viewModel.addPokemon(pokemon).onSuccess {
                                            Toast.makeText(context, "Pokémon agregado", Toast.LENGTH_SHORT).show()
                                            showDialog = false
                                        }.onFailure {
                                            Toast.makeText(context, "Error al agregar: ${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
                        ) {
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
                        Button(
                            onClick = {
                                scope.launch {
                                    selectedPokemon?.let { currentPokemon ->
                                        val updatedPokemon = currentPokemon.copy(
                                            name = newPokemonName,
                                            type = newPokemonType,
                                            abilities = newPokemonAbilities
                                        )
                                        viewModel.updatePokemon(updatedPokemon).onSuccess {
                                            Toast.makeText(context, "Pokémon actualizado", Toast.LENGTH_SHORT).show()
                                            showEditDialog = false
                                        }.onFailure {
                                            Toast.makeText(context, "Error al actualizar: ${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
                        ) {
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
