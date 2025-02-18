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
import com.agalvanmartin.pokedexapp.data.repositories.StadiumRepository
import kotlinx.coroutines.launch

@Composable
fun ListStadiumScreen(navController: NavController) {
    var stadiumList by remember { mutableStateOf<List<Stadium>>(emptyList()) }
    val scope = rememberCoroutineScope()

    val stadiumRepository = StadiumRepository()

    fun loadStadiums() {
        scope.launch {
            try {
                stadiumList = stadiumRepository.getAllStadiums().filter { it.name.isNotEmpty() }
            } catch (e: Exception) {
                Toast.makeText(navController.context, "Error al cargar los estadios.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        loadStadiums()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.Start)) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Listado de Estadios", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(stadiumList) { stadium ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF98FB98))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "ID: ${stadium.id}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Nombre: ${stadium.name}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Insignias: ${stadium.badges}", style = MaterialTheme.typography.bodyMedium)
                        }

                        Row {
                            IconButton(onClick = { navController.navigate("modifyStadiumScreen") }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Black)
                            }
                            IconButton(onClick = { navController.navigate("deleteStadiumScreen") }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}
