package com.example.ticketway.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: CacheDatabase? = null

    fun getDatabase(context: Context): CacheDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                CacheDatabase::class.java,
                CacheDatabase.DATABASE_NAME
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
