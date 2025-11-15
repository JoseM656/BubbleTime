package com.example.bubbletime.data.local.dao

import androidx.room.*
import com.example.bubbletime.data.local.entity.LinkEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones sobre la tabla links
 */
@Dao
interface LinkDao {

    /**
     * Obtiene todos los links como Flow
     */
    @Query("SELECT * FROM links")
    fun getAllLinks(): Flow<List<LinkEntity>>

    /**
     * Obtiene todos los links que involucran una burbuja específica
     */
    @Query("SELECT * FROM links WHERE bubbleAId = :bubbleId OR bubbleBId = :bubbleId")
    suspend fun getLinksForBubble(bubbleId: String): List<LinkEntity>

    /**
     * Inserta un nuevo link
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(link: LinkEntity)

    /**
     * Elimina un link
     */
    @Delete
    suspend fun deleteLink(link: LinkEntity)

    /**
     * Elimina todos los links
     */
    @Query("DELETE FROM links")
    suspend fun deleteAllLinks()

    /**
     * Verifica si existe un link entre dos burbujas
     */
    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM links 
            WHERE (bubbleAId = :bubbleAId AND bubbleBId = :bubbleBId)
               OR (bubbleAId = :bubbleBId AND bubbleBId = :bubbleAId)
        )
    """)
    suspend fun linkExists(bubbleAId: String, bubbleBId: String): Boolean
}