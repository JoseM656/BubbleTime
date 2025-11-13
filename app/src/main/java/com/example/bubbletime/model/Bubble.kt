package com.example.bubbletime.model

data class Bubble(
    val name: String,           // País o ciudad
    val time: String,           // Hora local
    val temperature: String? = null  // Temperatura opcional
)
