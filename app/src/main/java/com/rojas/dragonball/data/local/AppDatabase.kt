package com.rojas.dragonball.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        // Una sola instancia de la base de datos para toda la app
        @Volatile
        private var instancia: AppDatabase? = null

        fun obtener(context: Context): AppDatabase {
            return instancia ?: synchronized(this) {
                val nueva = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dragonball.db"
                ).build()
                instancia = nueva
                nueva
            }
        }
    }
}
