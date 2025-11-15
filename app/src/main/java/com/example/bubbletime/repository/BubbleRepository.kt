
package com.example.bubbletime.repository

import com.example.bubbletime.data.local.dao.BubbleDao
import com.example.bubbletime.data.local.dao.LinkDao
import com.example.bubbletime.data.local.entity.BubbleEntity
import com.example.bubbletime.data.local.entity.LinkEntity
import com.example.bubbletime.data.local.entity.toBubble
import com.example.bubbletime.data.local.entity.toEntity
import com.example.bubbletime.model.Bubble
import com.example.bubbletime.model.Link
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository que actúa como fuente única de verdad
 * Coordina entre la base de datos y el ViewModel
 */
class BubbleRepository(
    private val bubbleDao: BubbleDao,
    private val linkDao: LinkDao
) {

    // ========================================
    // BURBUJAS
    // ========================================

    /**
     * Obtiene todas las burbujas como Flow
     * Se actualiza automáticamente cuando cambia la BD
     */
    val allBubbles: Flow<List<Bubble>> = bubbleDao.getAllBubbles().map { entities ->
        entities.map { it.toBubble() }
    }

    /**
     * Agrega una nueva burbuja
     */
    suspend fun insertBubble(bubble: Bubble) {
        bubbleDao.insertBubble(bubble.toEntity())
    }

    /**
     * Actualiza una burbuja existente
     */
    suspend fun updateBubble(bubble: Bubble) {
        bubbleDao.updateBubble(bubble.toEntity())
    }

    /**
     * Elimina una burbuja
     * Los links asociados se eliminan automáticamente (CASCADE)
     */
    suspend fun deleteBubble(bubble: Bubble) {
        bubbleDao.deleteBubble(bubble.toEntity())
    }

    /**
     * Obtiene una burbuja por ID
     */
    suspend fun getBubbleById(id: String): Bubble? {
        return bubbleDao.getBubbleById(id)?.toBubble()
    }

    // ========================================
    // LINKS
    // ========================================

    /**
     * Obtiene todos los links como Flow
     * NOTA: Devuelve LinkEntity porque necesitamos convertirlos
     * a Link (que requiere objetos Bubble completos)
     */
    val allLinksEntities: Flow<List<LinkEntity>> = linkDao.getAllLinks()

    /**
     * Agrega un nuevo link
     */
    suspend fun insertLink(bubbleAId: String, bubbleBId: String, timeDifference: Long) {
        val link = LinkEntity(
            id = "${bubbleAId}_${bubbleBId}", // ID compuesto
            bubbleAId = bubbleAId,
            bubbleBId = bubbleBId,
            timeDifference = timeDifference
        )
        linkDao.insertLink(link)
    }

    /**
     * Elimina un link
     */
    suspend fun deleteLink(linkEntity: LinkEntity) {
        linkDao.deleteLink(linkEntity)
    }

    /**
     * Verifica si existe un link entre dos burbujas
     */
    suspend fun linkExists(bubbleAId: String, bubbleBId: String): Boolean {
        return linkDao.linkExists(bubbleAId, bubbleBId)
    }

    /**
     * Obtiene los IDs de burbujas de un link y los convierte a objetos Link completos
     */
    suspend fun getLinkWithBubbles(linkEntity: LinkEntity): Link? {
        val bubbleA = bubbleDao.getBubbleById(linkEntity.bubbleAId)?.toBubble()
        val bubbleB = bubbleDao.getBubbleById(linkEntity.bubbleBId)?.toBubble()

        return if (bubbleA != null && bubbleB != null) {
            Link(
                id = linkEntity.id,
                bubbleA = bubbleA,
                bubbleB = bubbleB,
                timeDifference = linkEntity.timeDifference
            )
        } else {
            null
        }
    }
}