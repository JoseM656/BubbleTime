package com.example.bubbletime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bubbletime.screens.TimeZoneSearchBar
import com.example.bubbletime.viewmodel.BubbleTimeViewModel
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    private val viewModel: BubbleTimeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BubbleTimeApp(viewModel)
        }
    }
}

@Composable
fun BubbleTimeApp(viewModel: BubbleTimeViewModel) {
    var showSearchBar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // -------------------------
        // TÍTULO Y BOTÓN AGREGAR
        // -------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Bubble Time",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(onClick = { showSearchBar = !showSearchBar }) {
                Text(if (showSearchBar) "Cancelar" else "+ Agregar")
            }
        }

        Spacer(Modifier.height(16.dp))

        // -------------------------
        // BARRA DE BÚSQUEDA
        // -------------------------
        if (showSearchBar) {
            TimeZoneSearchBar(
                onZoneSelected = { zone ->
                    viewModel.addBubble(zone)
                    showSearchBar = false
                }
            )
            Spacer(Modifier.height(16.dp))
        }

        // -------------------------
        // INSTRUCCIONES
        // -------------------------
        if (viewModel.bubbles.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "👋 ¡Bienvenido a Bubble Time!",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = """
                            1. Toca "+ Agregar" para buscar zonas horarias
                            2. Agrega varias burbujas
                            3. Toca dos burbujas para conectarlas y ver la diferencia horaria
                        """.trimIndent()
                    )
                }
            }
        } else {
            Text(
                text = if (viewModel.selectedBubbleForLink != null)
                    "Selecciona otra burbuja para conectar"
                else
                    "Toca dos burbujas para compararlas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(16.dp))

        // -------------------------
        // LISTA DE BURBUJAS
        // -------------------------
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viewModel.bubbles, key = { it.id }) { bubble ->
                BubbleCard(
                    bubble = bubble,
                    isSelected = viewModel.selectedBubbleForLink?.id == bubble.id,
                    onBubbleClick = { viewModel.toggleBubbleSelection(bubble) },
                    onDeleteClick = { viewModel.removeBubble(bubble) }
                )
            }
        }

        // -------------------------
        // LISTA DE CONEXIONES
        // -------------------------
        if (viewModel.links.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Conexiones",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.links, key = { it.id }) { link ->
                    LinkCard(
                        link = link,
                        onDeleteClick = { viewModel.removeLink(link) }
                    )
                }
            }
        }

        // -------------------------
        // BOTÓN ACTUALIZAR
        // -------------------------
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { viewModel.updateAllTimes() },
            modifier = Modifier.fillMaxWidth(),
            enabled = viewModel.bubbles.isNotEmpty()
        ) {
            Text("🔄 Actualizar horarios")
        }
    }
}

@Composable
fun BubbleCard(
    bubble: com.example.bubbletime.model.Bubble,
    isSelected: Boolean,
    onBubbleClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isSelected) {
                    Modifier.border(3.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                } else {
                    Modifier
                }
            ),
        onClick = onBubbleClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = bubble.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = bubble.timeZone,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = bubble.time,
                    style = MaterialTheme.typography.headlineSmall
                )

                IconButton(onClick = onDeleteClick) {
                    Text("❌", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun LinkCard(
    link: com.example.bubbletime.model.Link,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${link.bubbleA.name} ↔ ${link.bubbleB.name}",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Diferencia: ${link.timeDifference}h",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onDeleteClick) {
                Text("🗑️", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}