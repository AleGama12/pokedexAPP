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
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.agalvanmartin.pokedexapp.data.repositories.StadiumRepository
import kotlinx.coroutines.launch

@Composable
fun ListStadiumScreen(navController: NavController) {
    var stadiumList by remember { mutableStateOf<List<Stadium>>(emptyList()) }
    val scope = rememberCoroutineScope()

    val stadiumRepository = StadiumRepository()

    // Función para cargar los estadios desde Firestore
    fun loadStadiums() {
        scope.launch {
            try {
                stadiumList = stadiumRepository.getAllStadiums()
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
        // Flecha para regresar
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.Start)) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Listado de Estadios", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar los estadios en una lista
        LazyColumn {
            items(stadiumList) { stadium ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .shadow(5.dp, shape = MaterialTheme.shapes.medium), // Añadir sombra para profundidad
                    shape = MaterialTheme.shapes.medium, // Bordes redondeados
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF98FB98) // Color de fondo del card
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
                            Text(text = "ID: ${stadium.id}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Nombre: ${stadium.name}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Insignias: ${stadium.badges}", style = MaterialTheme.typography.bodyMedium)
                        }

                        // Botones para modificar o eliminar el estadio
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.End
                        ) {
                            // Botón para modificar el estadio
                            IconButton(
                                onClick = {
                                    navController.navigate("modifyStadiumScreen/${stadium.id}")
                                },
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Icon(Icons.Filled.Edit, contentDescription = "Modificar Estadio", tint = Color(0xFF00838F))
                            }

                            // Botón para eliminar el estadio
                            IconButton(
                                onClick = {
                                    navController.navigate("deleteStadiumScreen/${stadium.id}")
                                },
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Icon(Icons.Filled.Delete, contentDescription = "Eliminar Estadio", tint = Color(0xFFB71C1C))
                            }
                        }
                    }
                }
            }
        }
    }
}
