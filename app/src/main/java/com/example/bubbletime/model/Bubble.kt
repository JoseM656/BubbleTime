package com.example.bubbletime.model

data class Bubble(
    val id: String,              // ID único para identificar la burbuja
    val name: String,            // Nombre a mostrar (ej: "Buenos Aires")
    val timeZone: String,        // Zona horaria completa (ej: "America/Argentina/Buenos_Aires")
    val time: String,            // Hora local (ej: "10:37")
    val temperature: String? = null  // Temperatura opcional (para futuras features)
)