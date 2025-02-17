package com.agalvanmartin.pokedexapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.agalvanmartin.pokedexapp.data.repositories.StadiumRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteStadiumScreen(navController: NavController) {
    var stadiumId by remember { mutableStateOf("") }
    var stadiumName by remember { mutableStateOf("") }
    var stadiumBadges by remember { mutableStateOf("") }
    var stadiumFound by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val stadiumRepository = StadiumRepository()

    // Función para buscar el estadio
    fun searchStadium() {
        scope.launch {
            try {
                val stadium = stadiumRepository.getAllStadiums().find { it.id == stadiumId }
                if (stadium != null) {
                    stadiumName = stadium.name
                    stadiumBadges = stadium.badges
                    stadiumFound = true
                } else {
                    stadiumFound = false
                    Toast.makeText(navController.context, "Estadio no encontrado", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(navController.context, "Error al buscar el estadio.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para eliminar el estadio
    fun deleteStadium() {
        if (stadiumId.isEmpty()) {
            Toast.makeText(navController.context, "Por favor ingresa un ID válido", Toast.LENGTH_SHORT).show()
            return
        }

        scope.launch {
            try {
                stadiumRepository.deleteStadium(stadiumId)
                Toast.makeText(navController.context, "Estadio eliminado exitosamente!", Toast.LENGTH_SHORT).show()
                navController.navigate("listStadiumScreen")
            } catch (e: Exception) {
                Toast.makeText(navController.context, "Error al eliminar el estadio.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Flecha para regresar
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.Start)) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Eliminar Estadio", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para el ID del estadio
        TextField(
            value = stadiumId,
            onValueChange = { stadiumId = it },
            label = { Text("ID del Estadio") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF98FB98),
                focusedIndicatorColor = Color(0xFF00838F),
                unfocusedIndicatorColor = Color(0xFF00838F),
                focusedLabelColor = Color(0xFF00838F),
                unfocusedLabelColor = Color(0xFF00838F),
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para buscar el estadio
        Button(
            onClick = { searchStadium() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00838F))
        ) {
            Icon(Icons.Filled.Delete, contentDescription = "Buscar Estadio", modifier = Modifier.padding(end = 8.dp))
            Text(text = "Buscar Estadio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Si el estadio es encontrado, mostrar sus detalles y un botón para confirmar eliminación
        if (stadiumFound) {
            Text(text = "Nombre: $stadiumName", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Insignias: $stadiumBadges", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { deleteStadium() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
            ) {
                Text(text = "Confirmar Eliminación")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDeleteStadiumScreen() {
    DeleteStadiumScreen(navController = rememberNavController())
}
