package com.example.bubbletime.model

data class Link(
    val id: String,              // ID único del link
    val bubbleA: Bubble,
    val bubbleB: Bubble,
    val timeDifference: Long     // Diferencia en horas
)