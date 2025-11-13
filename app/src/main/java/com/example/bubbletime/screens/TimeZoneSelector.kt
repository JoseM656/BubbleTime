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
fun TimeZoneSelector(onZoneSelected: (String) -> Unit) {

    var searchQuery by remember { mutableStateOf("") }

    val allZones = remember { ZoneId.getAvailableZoneIds().toList().sorted() }
    val filteredZones = allZones.filter { it.contains(searchQuery, ignoreCase = true) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar zona horaria") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(filteredZones) { zone ->
                Text(
                    text = zone,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onZoneSelected(zone) }
                        .padding(12.dp)
                )
                Divider()
            }
        }
    }
}
