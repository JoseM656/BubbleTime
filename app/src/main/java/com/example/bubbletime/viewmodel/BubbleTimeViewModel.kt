package com.example.bubbletime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.bubbletime.model.Bubble
import com.example.bubbletime.model.Link
import com.example.bubbletime.data.DateTimeCollector
import com.example.bubbletime.repository.BubbleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel que gestiona el estado de todas las burbujas y sus conexiones
 * Ahora con persistencia mediante Room
 */
class BubbleTimeViewModel(
    private val repository: BubbleRepository
) : ViewModel() {

    // Estado de burbujas (desde la base de datos)
    private val _bubbles = MutableStateFlow<List<Bubble>>(emptyList())
    val bubbles: StateFlow<List<Bubble>> = _bubbles.asStateFlow()

    // Estado de links (desde la base de datos)
    private val _links = MutableStateFlow<List<Link>>(emptyList())
    val links: StateFlow<List<Link>> = _links.asStateFlow()

    // Burbuja seleccionada para crear un link
    var selectedBubbleForLink by mutableStateOf<Bubble?>(null)
        private set

    init {
        // Observar cambios en la base de datos
        observeBubbles()
        observeLinks()
    }

    /**
     * Observa los cambios en las burbujas desde la BD
     */
    private fun observeBubbles() {
        viewModelScope.launch {
            repository.allBubbles.collect { bubbleList ->
                _bubbles.value = bubbleList
            }
        }
    }

    /**
     * Observa los cambios en los links desde la BD
     */
    private fun observeLinks() {
        viewModelScope.launch {
            repository.allLinksEntities.collect { linkEntities ->
                // Convertir LinkEntity a Link con burbujas completas
                val links = linkEntities.mapNotNull { linkEntity ->
                    repository.getLinkWithBubbles(linkEntity)
                }
                _links.value = links
            }
        }
    }

    /**
     * Agrega una nueva burbuja con la zona horaria especificada
     */
    fun addBubble(timeZone: String) {
        viewModelScope.launch {
            try {
                val time = DateTimeCollector.getLocalTime(timeZone)
                val bubble = Bubble(
                    id = UUID.randomUUID().toString(),
                    name = formatZoneName(timeZone),
                    timeZone = timeZone,
                    time = time
                )
                repository.insertBubble(bubble)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Elimina una burbuja
     * Los links se eliminan automáticamente por CASCADE
     */
    fun removeBubble(bubble: Bubble) {
        viewModelScope.launch {
            repository.deleteBubble(bubble)
        }
    }

    /**
     * Selecciona una burbuja para crear un link
     * Si ya hay una seleccionada, crea el link entre ambas
     */
    fun toggleBubbleSelection(bubble: Bubble) {
        when {
            selectedBubbleForLink == null -> {
                selectedBubbleForLink = bubble
            }
            selectedBubbleForLink?.id == bubble.id -> {
                selectedBubbleForLink = null
            }
            else -> {
                createLink(selectedBubbleForLink!!, bubble)
                selectedBubbleForLink = null
            }
        }
    }

    /**
     * Crea un link entre dos burbujas
     */
    private fun createLink(bubbleA: Bubble, bubbleB: Bubble) {
        viewModelScope.launch {
            // Verificar que el link no exista
            val exists = repository.linkExists(bubbleA.id, bubbleB.id)

            if (!exists) {
                val timeDiff = DateTimeCollector.getTimeDifference(
                    bubbleA.timeZone,
                    bubbleB.timeZone
                )
                repository.insertLink(bubbleA.id, bubbleB.id, timeDiff)
            }
        }
    }

    /**
     * Elimina un link específico
     */
    fun removeLink(link: Link) {
        viewModelScope.launch {
            // Necesitamos reconstruir el LinkEntity para eliminarlo
            val linkEntity = com.example.bubbletime.data.local.entity.LinkEntity(
                id = link.id,
                bubbleAId = link.bubbleA.id,
                bubbleBId = link.bubbleB.id,
                timeDifference = link.timeDifference
            )
            repository.deleteLink(linkEntity)
        }
    }

    /**
     * Actualiza los horarios de todas las burbujas
     */
    fun updateAllTimes() {
        viewModelScope.launch {
            _bubbles.value.forEach { bubble ->
                try {
                    val newTime = DateTimeCollector.getLocalTime(bubble.timeZone)
                    val updatedBubble = bubble.copy(time = newTime)
                    repository.updateBubble(updatedBubble)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Formatea el nombre de la zona horaria para mostrar
     */
    private fun formatZoneName(timeZone: String): String {
        return timeZone
            .substringAfterLast("/")
            .replace("_", " ")
    }
}