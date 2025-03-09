package com.agalvanmartin.pokedexapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class SortOrder {
    ID_ASC, ID_DESC, NAME_ASC, NAME_DESC
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsHeader(
    totalItems: Int,
    currentSort: SortOrder,
    onSortChange: (SortOrder) -> Unit,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    var showSortMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth().padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total: $totalItems",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            
            Box {
                IconButton(onClick = { showSortMenu = true }) {
                    Icon(
                        Icons.Default.Sort,
                        contentDescription = "Ordenar",
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { showSortMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("ID (Ascendente)") },
                        onClick = {
                            onSortChange(SortOrder.ID_ASC)
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("ID (Descendente)") },
                        onClick = {
                            onSortChange(SortOrder.ID_DESC)
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Nombre (A-Z)") },
                        onClick = {
                            onSortChange(SortOrder.NAME_ASC)
                            showSortMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Nombre (Z-A)") },
                        onClick = {
                            onSortChange(SortOrder.NAME_DESC)
                            showSortMenu = false
                        }
                    )
                }
            }
        }
    }
} 