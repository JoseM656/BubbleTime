package com.example.bubbletime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.bubbletime.model.Bubble
import com.example.bubbletime.model.Link
import com.example.bubbletime.data.DateTimeCollector
import java.util.UUID

/**
 * ViewModel que gestiona el estado de todas las burbujas y sus conexiones
 */
class BubbleTimeViewModel : ViewModel() {

    // Lista de burbujas activas
    val bubbles = mutableStateListOf<Bubble>()

    // Lista de conexiones entre burbujas
    val links = mutableStateListOf<Link>()

    // Burbuja seleccionada para crear un link
    var selectedBubbleForLink by mutableStateOf<Bubble?>(null)
        private set

    /**
     * Agrega una nueva burbuja con la zona horaria especificada
     */
    fun addBubble(timeZone: String) {
        try {
            val time = DateTimeCollector.getLocalTime(timeZone)
            val bubble = Bubble(
                id = UUID.randomUUID().toString(),
                name = formatZoneName(timeZone),
                timeZone = timeZone,
                time = time
            )
            bubbles.add(bubble)
        } catch (e: Exception) {
            // Zona horaria inválida
            e.printStackTrace()
        }
    }

    /**
     * Elimina una burbuja y todas sus conexiones
     */
    fun removeBubble(bubble: Bubble) {
        // Eliminar todos los links que involucran esta burbuja
        links.removeAll { link ->
            link.bubbleA.id == bubble.id || link.bubbleB.id == bubble.id
        }
        bubbles.remove(bubble)
    }

    /**
     * Selecciona una burbuja para crear un link
     * Si ya hay una seleccionada, crea el link entre ambas
     */
    fun toggleBubbleSelection(bubble: Bubble) {
        when {
            selectedBubbleForLink == null -> {
                // Primera burbuja seleccionada
                selectedBubbleForLink = bubble
            }
            selectedBubbleForLink?.id == bubble.id -> {
                // Deseleccionar la misma burbuja
                selectedBubbleForLink = null
            }
            else -> {
                // Segunda burbuja: crear link
                createLink(selectedBubbleForLink!!, bubble)
                selectedBubbleForLink = null
            }
        }
    }

    /**
     * Crea un link entre dos burbujas
     */
    private fun createLink(bubbleA: Bubble, bubbleB: Bubble) {
        // Verificar que el link no exista ya
        val linkExists = links.any { link ->
            (link.bubbleA.id == bubbleA.id && link.bubbleB.id == bubbleB.id) ||
                    (link.bubbleA.id == bubbleB.id && link.bubbleB.id == bubbleA.id)
        }

        if (!linkExists) {
            val timeDiff = DateTimeCollector.getTimeDifference(
                bubbleA.timeZone,
                bubbleB.timeZone
            )
            val link = Link(
                id = UUID.randomUUID().toString(),
                bubbleA = bubbleA,
                bubbleB = bubbleB,
                timeDifference = timeDiff
            )
            links.add(link)
        }
    }

    /**
     * Elimina un link específico
     */
    fun removeLink(link: Link) {
        links.remove(link)
    }

    /**
     * Actualiza los horarios de todas las burbujas
     */
    fun updateAllTimes() {
        bubbles.forEachIndexed { index, bubble ->
            try {
                val newTime = DateTimeCollector.getLocalTime(bubble.timeZone)
                bubbles[index] = bubble.copy(time = newTime)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Formatea el nombre de la zona horaria para mostrar
     * Ejemplo: "America/Argentina/Buenos_Aires" -> "Buenos Aires"
     */
    private fun formatZoneName(timeZone: String): String {
        return timeZone
            .substringAfterLast("/")
            .replace("_", " ")
    }
}