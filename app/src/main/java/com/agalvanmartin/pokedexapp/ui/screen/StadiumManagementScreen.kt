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

data class Stadium(
    val id: String = "",
    val name: String = "",
    val capacity: String = "",
    val insignia: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StadiumManagementScreen(navController: NavController) {
    var stadiumList by remember { mutableStateOf<List<Stadium>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var newStadiumId by remember { mutableStateOf("") }
    var newStadiumName by remember { mutableStateOf("") }
    var newStadiumInsignia by remember { mutableStateOf("") }
    var newStadiumCapacity by remember { mutableStateOf("") }
    var selectedStadium by remember { mutableStateOf<Stadium?>(null) }

    fun loadStadiums() {
        val db = FirebaseFirestore.getInstance()
        db.collection("stadiums")
            .get()
            .addOnSuccessListener { result ->
                val stadiums = result.documents.mapNotNull { it.toObject(Stadium::class.java) }
                stadiumList = stadiums.filter { it.name.isNotEmpty() }.sortedBy { it.id.toIntOrNull() ?: Int.MAX_VALUE }
            }
            .addOnFailureListener {
                Toast.makeText(
                    navController.context,
                    "Error al cargar los estadios.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun addStadium() {
        if (newStadiumId.isNotEmpty() && newStadiumName.isNotEmpty() && newStadiumInsignia.isNotEmpty() && newStadiumCapacity.isNotEmpty()) {
            val db = FirebaseFirestore.getInstance()
            val newStadium = hashMapOf(
                "id" to newStadiumId,
                "name" to newStadiumName,
                "insignia" to newStadiumInsignia,
                "capacity" to newStadiumCapacity
            )
            db.collection("stadiums").add(newStadium)
                .addOnSuccessListener {
                    Toast.makeText(navController.context, "Estadio agregado.", Toast.LENGTH_SHORT)
                        .show()
                    loadStadiums()
                    showDialog = false
                }
                .addOnFailureListener {
                    Toast.makeText(
                        navController.context,
                        "Error al agregar estadio.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    fun updateStadium() {
        selectedStadium?.let { stadium ->
            val db = FirebaseFirestore.getInstance()
            db.collection("stadiums").whereEqualTo("id", stadium.id).get()
                .addOnSuccessListener { result ->
                    for (document in result.documents) {
                        db.collection("stadiums").document(document.id).update(
                            mapOf(
                                "name" to newStadiumName,
                                "insignia" to newStadiumInsignia,
                                "capacity" to newStadiumCapacity
                            )
                        ).addOnSuccessListener {
                            Toast.makeText(
                                navController.context,
                                "Estadio actualizado.",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadStadiums()
                            showEditDialog = false
                        }.addOnFailureListener {
                            Toast.makeText(
                                navController.context,
                                "Error al actualizar estadio.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }

    fun deleteStadium(stadium: Stadium) {
        val db = FirebaseFirestore.getInstance()
        db.collection("stadiums").whereEqualTo("id", stadium.id).get()
            .addOnSuccessListener { result ->
                for (document in result.documents) {
                    db.collection("stadiums").document(document.id).delete()
                        .addOnSuccessListener {
                            Toast.makeText(
                                navController.context,
                                "Estadio eliminado.",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadStadiums()
                            showDeleteDialog = false
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                navController.context,
                                "Error al eliminar estadio.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
    }

    LaunchedEffect(Unit) { loadStadiums() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Estadios", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("main") }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF9800))
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                newStadiumId = ""
                newStadiumName = ""
                newStadiumInsignia = ""
                newStadiumCapacity = ""
                showDialog = true
            }, containerColor = Color(0xFFFF9800)) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Estadio", tint = Color.White)
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            items(stadiumList) { stadium ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0B2))
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
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = Color.Black
                                )
                            }
                            IconButton(onClick = {
                                selectedStadium = stadium; showDeleteDialog = true
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog && selectedStadium != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar Estadio") },
            text = { Text("¿Seguro que deseas eliminar a ${selectedStadium?.name}?") },
            confirmButton = {
                Button(
                    onClick = { deleteStadium(selectedStadium!!) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
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
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Agregar Nuevo Estadio",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFFF9800)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = newStadiumId,
                        onValueChange = { newStadiumId = it },
                        label = { Text("ID") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = newStadiumName,
                        onValueChange = { newStadiumName = it },
                        label = { Text("Nombre") })
                    OutlinedTextField(
                        value = newStadiumInsignia,
                        onValueChange = { newStadiumInsignia = it },
                        label = { Text("Insignia") })
                    OutlinedTextField(
                        value = newStadiumCapacity,
                        onValueChange = { newStadiumCapacity = it },
                        label = { Text("Capacidad") })
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { addStadium() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726))
                        ) {
                            Text("Agregar", color = Color.White)
                        }
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
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
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Editar Estadio",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFFF9800)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = newStadiumName,
                        onValueChange = { newStadiumName = it },
                        label = { Text("Nombre") })
                    OutlinedTextField(
                        value = newStadiumInsignia,
                        onValueChange = { newStadiumInsignia = it },
                        label = { Text("Insignia") })
                    OutlinedTextField(
                        value = newStadiumCapacity,
                        onValueChange = { newStadiumCapacity = it },
                        label = { Text("Capacidad") })
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { updateStadium() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726))
                        ) {
                            Text("Actualizar", color = Color.White)
                        }
                        Button(
                            onClick = { showEditDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Cancelar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}