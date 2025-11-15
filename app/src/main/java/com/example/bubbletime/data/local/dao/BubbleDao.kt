package com.example.bubbletime.data.local.dao

import androidx.room.*
import com.example.bubbletime.data.local.entity.BubbleEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones sobre la tabla bubbles
 */
@Dao
interface BubbleDao {

    /**
     * Obtiene todas las burbujas como Flow (se actualiza automáticamente)
     */
    @Query("SELECT * FROM bubbles ORDER BY name ASC")
    fun getAllBubbles(): Flow<List<BubbleEntity>>

    /**
     * Obtiene una burbuja por ID
     */
    @Query("SELECT * FROM bubbles WHERE id = :bubbleId")
    suspend fun getBubbleById(bubbleId: String): BubbleEntity?

    /**
     * Inserta una nueva burbuja
     * OnConflictStrategy.REPLACE: Si ya existe, la reemplaza
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBubble(bubble: BubbleEntity)

    /**
     * Inserta múltiples burbujas
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBubbles(bubbles: List<BubbleEntity>)

    /**
     * Actualiza una burbuja existente
     */
    @Update
    suspend fun updateBubble(bubble: BubbleEntity)

    /**
     * Elimina una burbuja
     */
    @Delete
    suspend fun deleteBubble(bubble: BubbleEntity)

    /**
     * Elimina todas las burbujas
     */
    @Query("DELETE FROM bubbles")
    suspend fun deleteAllBubbles()
}