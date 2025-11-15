package com.example.bubbletime.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Entidad Room que representa un Link en la base de datos
 * Usa Foreign Keys para mantener integridad referencial
 */
@Entity(
    tableName = "links",
    foreignKeys = [
        ForeignKey(
            entity = BubbleEntity::class,
            parentColumns = ["id"],
            childColumns = ["bubbleAId"],
            onDelete = ForeignKey.CASCADE // Si eliminas una burbuja, se eliminan sus links
        ),
        ForeignKey(
            entity = BubbleEntity::class,
            parentColumns = ["id"],
            childColumns = ["bubbleBId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("bubbleAId"),
        Index("bubbleBId")
    ]
)
data class LinkEntity(
    @PrimaryKey
    val id: String,
    val bubbleAId: String, // ID de la primera burbuja
    val bubbleBId: String, // ID de la segunda burbuja
    val timeDifference: Long
)