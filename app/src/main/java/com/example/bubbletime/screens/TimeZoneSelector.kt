package com.example.bubbletime.screens

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.ZoneId

@Composable
fun TimeZoneSearchBar(onZoneSelected: (String) -> Unit) {

    var query by remember { mutableStateOf("") }

    val allZones = remember { ZoneId.getAvailableZoneIds().sorted().toList() }
    val filteredZones = remember(query) {
        allZones.filter { it.contains(query, ignoreCase = true) }
    }

    Column {

        // ---- SEARCHBAR ----
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar zona horaria") },
            placeholder = { Text("Ej: America/Argentina/Buenos_Aires") }
        )

        // ---- LISTA SOLO SI SE ESCRIBE ----
        if (query.isNotBlank()) {
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // Evita scroll infinito
            ) {
                items(filteredZones) { zone ->
                    Text(
                        text = zone,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onZoneSelected(zone)
                                query = "" // Ocultar lista
                            }
                            .padding(12.dp)
                    )
                    Divider()
                }
            }
        }
    }
}
