package com.doner.paketdagitim

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GunlukRapor::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun raporDao(): RaporDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "paket_dagitim_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
