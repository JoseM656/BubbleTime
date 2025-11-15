package com.example.bubbletime.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bubbletime.model.Bubble

/**
 * Entidad Room que representa una Bubble en la base de datos
 */
@Entity(tableName = "bubbles")
data class BubbleEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val timeZone: String,
    val time: String,
    val temperature: String? = null
)

// Funciones de conversión entre Entity y Model
fun BubbleEntity.toBubble(): Bubble {
    return Bubble(
        id = id,
        name = name,
        timeZone = timeZone,
        time = time,
        temperature = temperature
    )
}

fun Bubble.toEntity(): BubbleEntity {
    return BubbleEntity(
        id = id,
        name = name,
        timeZone = timeZone,
        time = time,
        temperature = temperature
    )
}