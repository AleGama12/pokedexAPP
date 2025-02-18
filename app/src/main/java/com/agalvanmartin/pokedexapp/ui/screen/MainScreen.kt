package com.agalvanmartin.pokedexapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Definir los colores
val LightBlue = Color(0xFF87CEFA) // Azul más suave para los Pokémon
val LightGreen = Color(0xFF98FB98) // Verde más suave para los Estadios

@Composable
fun MainScreen(navController: NavController, navigateToLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Flecha para regresar al Login
        IconButton(onClick = { navigateToLogin() }, modifier = Modifier.align(Alignment.Start)) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Regresar al login", tint = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Título
        Text(
            text = "Bienvenido a Pokedex",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Título para Pokémon
        Text(
            text = "Gestión de Pokémon",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Botones para Pokémon
        Button(
            onClick = { navController.navigate("addPokemonScreen") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar Pokémon", modifier = Modifier.padding(end = 8.dp))
            Text(text = "Agregar Pokémon")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("modifyPokemonScreen") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Modificar Pokémon", modifier = Modifier.padding(end = 8.dp))
            Text(text = "Modificar Pokémon")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("deletePokemonScreen") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
        ) {
            Icon(Icons.Filled.Delete, contentDescription = "Eliminar Pokémon", modifier = Modifier.padding(end = 8.dp))
            Text(text = "Eliminar Pokémon")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("listPokemonScreen") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
        ) {
            Icon(Icons.Filled.List, contentDescription = "Listado de Pokémon", modifier = Modifier.padding(end = 8.dp))
            Text(text = "Listado de Pokémon")
        }

        // Título para Estadios
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Gestión de Estadios",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Botones para Estadios
        Button(
            onClick = { navController.navigate("addStadiumScreen") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = LightGreen)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar Estadio", modifier = Modifier.padding(end = 8.dp))
            Text(text = "Agregar Estadio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("modifyStadiumScreen") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = LightGreen)
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Modificar Estadio", modifier = Modifier.padding(end = 8.dp))
            Text(text = "Modificar Estadio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("deleteStadiumScreen") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = LightGreen)
        ) {
            Icon(Icons.Filled.Delete, contentDescription = "Eliminar Estadio", modifier = Modifier.padding(end = 8.dp))
            Text(text = "Eliminar Estadio")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("listStadiumScreen") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = LightGreen)
        ) {
            Icon(Icons.Filled.List, contentDescription = "Listado de Estadios", modifier = Modifier.padding(end = 8.dp))
            Text(text = "Listado de Estadios")
        }
    }
}