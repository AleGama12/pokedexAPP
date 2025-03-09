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
import com.agalvanmartin.pokedexapp.viewmodel.Stadium
import com.agalvanmartin.pokedexapp.viewmodel.StadiumViewModel
import com.agalvanmartin.pokedexapp.viewmodel.base.ViewState
import kotlinx.coroutines.launch
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import com.agalvanmartin.pokedexapp.ui.components.SearchBar
import com.agalvanmartin.pokedexapp.ui.components.StatsHeader
import com.agalvanmartin.pokedexapp.ui.components.SortOrder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun StadiumManagementScreen(navController: NavController, viewModel: StadiumViewModel = StadiumViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var currentSort by remember { mutableStateOf(SortOrder.ID_ASC) }
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var newStadiumId by remember { mutableStateOf("") }
    var newStadiumName by remember { mutableStateOf("") }
    var newStadiumCapacity by remember { mutableStateOf("") }
    var newStadiumInsignia by remember { mutableStateOf("") }
    var selectedStadium by remember { mutableStateOf<Stadium?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Estadios", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("main") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF9800))
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                newStadiumId = ""
                newStadiumName = ""
                newStadiumCapacity = ""
                newStadiumInsignia = ""
                showDialog = true
            }, containerColor = Color(0xFFFF9800)) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Estadio", tint = Color.White)
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
                            totalItems = currentState.data.stadiums.size,
                            currentSort = currentSort,
                            onSortChange = { currentSort = it },
                            containerColor = Color(0xFFFF9800)
                        )
                        
                        SearchBar(
                            searchText = searchText,
                            onSearchTextChange = { searchText = it }
                        )
                    }
                    
                    val filteredStadiums = currentState.data.stadiums
                        .filter { it.name.contains(searchText, ignoreCase = true) }
                        .let { stadiums ->
                            when (currentSort) {
                                SortOrder.ID_ASC -> stadiums.sortedBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }
                                SortOrder.ID_DESC -> stadiums.sortedByDescending { it.id.toIntOrNull() ?: Int.MIN_VALUE }
                                SortOrder.NAME_ASC -> stadiums.sortedBy { it.name }
                                SortOrder.NAME_DESC -> stadiums.sortedByDescending { it.name }
                            }
                        }
                    
                    items(filteredStadiums, key = { it.id }) { stadium ->
                        val interactionSource = remember { MutableInteractionSource() }
                        val isHovered by interactionSource.collectIsHoveredAsState()
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .animateItemPlacement()
                                .hoverable(interactionSource)
                                .animateContentSize(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0B2)),
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
                                    Text(text = "ID: ${stadium.id}", color = Color.Black)
                                    Text(text = "Nombre: ${stadium.name}", color = Color.Black)
                                    Text(text = "Capacidad: ${stadium.capacity}", color = Color.Black)
                                    Text(text = "Insignia: ${stadium.insignia}", color = Color.Black)
                                }
                                Row {
                                    IconButton(onClick = {
                                        selectedStadium = stadium
                                        newStadiumName = stadium.name
                                        newStadiumCapacity = stadium.capacity
                                        newStadiumInsignia = stadium.insignia
                                        showEditDialog = true
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Black)
                                    }
                                    IconButton(onClick = { selectedStadium = stadium; showDeleteDialog = true }) {
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

    if (showDeleteDialog && selectedStadium != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Estadio") },
            text = { Text("¿Seguro que deseas eliminar el estadio ${selectedStadium?.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            selectedStadium?.let { stadium ->
                                viewModel.deleteStadium(stadium).onSuccess {
                                    Toast.makeText(context, "Estadio eliminado", Toast.LENGTH_SHORT).show()
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
                    Text("Agregar Nuevo Estadio", style = MaterialTheme.typography.titleLarge, color = Color(0xFFFF9800))
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = newStadiumId, onValueChange = { newStadiumId = it }, label = { Text("ID") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = newStadiumName, onValueChange = { newStadiumName = it }, label = { Text("Nombre") })
                    OutlinedTextField(value = newStadiumCapacity, onValueChange = { newStadiumCapacity = it }, label = { Text("Capacidad") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = newStadiumInsignia, onValueChange = { newStadiumInsignia = it }, label = { Text("Insignia") })
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            onClick = {
                                scope.launch {
                                    if (newStadiumId.isNotEmpty() && newStadiumName.isNotEmpty() && 
                                        newStadiumCapacity.isNotEmpty() && newStadiumInsignia.isNotEmpty()) {
                                        val stadium = Stadium(
                                            id = newStadiumId,
                                            name = newStadiumName,
                                            capacity = newStadiumCapacity,
                                            insignia = newStadiumInsignia
                                        )
                                        viewModel.addStadium(stadium).onSuccess {
                                            Toast.makeText(context, "Estadio agregado", Toast.LENGTH_SHORT).show()
                                            showDialog = false
                                        }.onFailure {
                                            Toast.makeText(context, "Error al agregar: ${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
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
                    Text("Editar Estadio", style = MaterialTheme.typography.titleLarge, color = Color(0xFFFF9800))
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = newStadiumName, onValueChange = { newStadiumName = it }, label = { Text("Nombre") })
                    OutlinedTextField(value = newStadiumCapacity, onValueChange = { newStadiumCapacity = it }, label = { Text("Capacidad") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    OutlinedTextField(value = newStadiumInsignia, onValueChange = { newStadiumInsignia = it }, label = { Text("Insignia") })
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            onClick = {
                                scope.launch {
                                    selectedStadium?.let { currentStadium ->
                                        val updatedStadium = currentStadium.copy(
                                            name = newStadiumName,
                                            capacity = newStadiumCapacity,
                                            insignia = newStadiumInsignia
                                        )
                                        viewModel.updateStadium(updatedStadium).onSuccess {
                                            Toast.makeText(context, "Estadio actualizado", Toast.LENGTH_SHORT).show()
                                            showEditDialog = false
                                        }.onFailure {
                                            Toast.makeText(context, "Error al actualizar: ${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
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