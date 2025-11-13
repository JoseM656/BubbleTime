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
    var text by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Ingresar región (ej. Europe/Madrid)") }
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            try {
                val time = DateTimeCollector.getLocalTime(text)
                val bubble = Bubble(name = text, time = time)
                result = "Hora local en ${bubble.name}: ${bubble.time}"
            } catch (e: Exception) {
                result = "Error: región no válida"
            }
        }) {
            Text("Mostrar hora")
        }
        Spacer(Modifier.height(16.dp))
        Text(result)
    }
}
