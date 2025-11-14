package com.example.bubbletime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bubbletime.data.DateTimeCollector
import com.example.bubbletime.model.Bubble
import com.example.bubbletime.screens.TimeZoneSearchBar
import androidx.compose.foundation.clickable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BubbleTimeApp()
        }
    }
}


@Composable
fun BubbleTimeApp() {

    var region1 by remember { mutableStateOf("") }
    var region2 by remember { mutableStateOf("") }

    // ¿Qué campo se está editando actualmente?
    var selectedField by remember { mutableStateOf<String?>(null) }

    // Resultado final
    var result by remember { mutableStateOf("") }

    // Zona actualmente elegida desde la barra superior
    var selectedZone by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {

        // -------------------------
        //  BARRA SUPERIOR DE BUSQUEDA
        // -------------------------
        TimeZoneSearchBar(
            onZoneSelected = { zone ->
                selectedZone = zone

                if (selectedField == "region1") region1 = zone
                if (selectedField == "region2") region2 = zone

                selectedField = null // cerrar selección
            }
        )

        Spacer(Modifier.height(16.dp))


        Text("Comparar zonas horarias", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        // ---- CAMPO 1 ----
        OutlinedTextField(
            value = region1,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedField = "region1" },
            readOnly = true,
            label = { Text("Región 1") },
            placeholder = { Text("Elegir zona horaria") }
        )

        Spacer(Modifier.height(12.dp))

        // ---- CAMPO 2 ----
        OutlinedTextField(
            value = region2,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedField = "region2" },
            readOnly = true,
            label = { Text("Región 2") },
            placeholder = { Text("Elegir zona horaria") }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                try {
                    val dif = DateTimeCollector.getTimeDifference(region1, region2)
                    val h1 = DateTimeCollector.getLocalTime(region1)
                    val h2 = DateTimeCollector.getLocalTime(region2)

                    result = """
                        $region1: $h1
                        $region2: $h2
                        
                        Diferencia: $dif hs
                    """.trimIndent()

                } catch (e: Exception) {
                    result = "Error: alguna región no es válida."
                }
            },
            enabled = region1.isNotBlank() && region2.isNotBlank()
        ) {
            Text("Comparar")
        }

        Spacer(Modifier.height(16.dp))
        Text(result)
    }
}



