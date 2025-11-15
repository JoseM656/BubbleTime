
package com.example.bubbletime.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bubbletime.data.local.dao.BubbleDao
import com.example.bubbletime.data.local.dao.LinkDao
import com.example.bubbletime.data.local.entity.BubbleEntity
import com.example.bubbletime.data.local.entity.LinkEntity

/**
 * Base de datos principal de la aplicación
 */
@Database(
    entities = [BubbleEntity::class, LinkEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bubbleDao(): BubbleDao
    abstract fun linkDao(): LinkDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos (Singleton)
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bubble_time_database"
                )
                    .fallbackToDestructiveMigration() // En desarrollo: borra y recrea si hay cambios
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}