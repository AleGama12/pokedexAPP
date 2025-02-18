package com.agalvanmartin.pokedexapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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


data class Stadium(
    val id: String = "",
    val name: String = "",
    val badges: String = "", // Insignias
    val creationDate: Long = System.currentTimeMillis() // Fecha de creación
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStadiumScreen(navController: NavController) {
    var stadiumId by remember { mutableStateOf("") }
    var stadiumName by remember { mutableStateOf("") }
    var stadiumBadges by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val stadiumRepository = StadiumRepository()

    // Función para agregar un estadio
    fun addStadiumToFirestore() {
        if (stadiumId.isEmpty() || stadiumName.isEmpty() || stadiumBadges.isEmpty()) {
            Toast.makeText(navController.context, "Por favor ingresa todos los datos", Toast.LENGTH_SHORT).show()
            return
        }

        val newStadium = Stadium(id = stadiumId, name = stadiumName, badges = stadiumBadges)

        scope.launch {
            try {
                stadiumRepository.addStadium(newStadium)
                Toast.makeText(navController.context, "Estadio agregado exitosamente!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(navController.context, "Error al agregar el Estadio.", Toast.LENGTH_SHORT).show()
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

        Text(text = "Agregar Estadio", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para el ID del Estadio
        TextField(
            value = stadiumId,
            onValueChange = { stadiumId = it },
            label = { Text("ID del Estadio") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = LightGreen,
                focusedIndicatorColor = Color(0xFF00838F),
                unfocusedIndicatorColor = Color(0xFF00838F),
                focusedLabelColor = Color(0xFF00838F),
                unfocusedLabelColor = Color(0xFF00838F),
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para el nombre del Estadio
        TextField(
            value = stadiumName,
            onValueChange = { stadiumName = it },
            label = { Text("Nombre del Estadio") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = LightGreen,
                focusedIndicatorColor = Color(0xFF00838F),
                unfocusedIndicatorColor = Color(0xFF00838F),
                focusedLabelColor = Color(0xFF00838F),
                unfocusedLabelColor = Color(0xFF00838F),
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para las insignias del Estadio
        TextField(
            value = stadiumBadges,
            onValueChange = { stadiumBadges = it },
            label = { Text("Insignias del Estadio") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = LightGreen,
                focusedIndicatorColor = Color(0xFF00838F),
                unfocusedIndicatorColor = Color(0xFF00838F),
                focusedLabelColor = Color(0xFF00838F),
                unfocusedLabelColor = Color(0xFF00838F),
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para agregar el estadio
        Button(
            onClick = { addStadiumToFirestore() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00838F),
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar Estadio", modifier = Modifier.padding(end = 8.dp))
            Text(text = "Agregar Estadio")
        }
    }
}