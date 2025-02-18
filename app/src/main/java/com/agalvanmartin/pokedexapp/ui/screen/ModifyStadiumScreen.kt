package com.agalvanmartin.pokedexapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
fun ModifyStadiumScreen(navController: NavController) {
    var stadiumId by remember { mutableStateOf("") }
    var stadiumName by remember { mutableStateOf("") }
    var stadiumBadges by remember { mutableStateOf("") }
    var stadiumFound by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val stadiumRepository = StadiumRepository()
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
    fun updateStadium() {
        if (stadiumName.isEmpty() || stadiumBadges.isEmpty()) {
            Toast.makeText(navController.context, "Por favor ingresa todos los datos", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedStadium = Stadium(id = stadiumId, name = stadiumName, badges = stadiumBadges)

        scope.launch {
            try {
                stadiumRepository.updateStadium(updatedStadium)
                Toast.makeText(navController.context, "Estadio actualizado exitosamente!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(navController.context, "Error al actualizar el estadio.", Toast.LENGTH_SHORT).show()
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
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.Start)) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar", tint = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Modificar Estadio", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))
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
        Button(
            onClick = { searchStadium() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00838F))
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Buscar Estadio", modifier = Modifier.padding(end = 8.dp))
            Text(text = "Buscar Estadio")
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (stadiumFound) {
            TextField(
                value = stadiumName,
                onValueChange = { stadiumName = it },
                label = { Text("Nombre del Estadio") },
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

            TextField(
                value = stadiumBadges,
                onValueChange = { stadiumBadges = it },
                label = { Text("Insignias del Estadio") },
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

            Button(
                onClick = { updateStadium() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00838F),
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "Modificar Estadio", modifier = Modifier.padding(end = 8.dp))
                Text(text = "Modificar Estadio")
            }
        }
    }
}
