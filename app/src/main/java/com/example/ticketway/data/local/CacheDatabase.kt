package com.example.ticketway.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ticketway.data.local.dao.FixturesDao
import com.example.ticketway.data.local.dao.SquadsDao
import com.example.ticketway.data.local.dao.StandingsDao
import com.example.ticketway.data.local.entities.FixtureEntity
import com.example.ticketway.data.local.entities.SquadEntity
import com.example.ticketway.data.local.entities.StandingEntity


@Database(
    entities = [FixtureEntity::class, SquadEntity::class, StandingEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CacheDatabase : RoomDatabase() {

    abstract fun fixturesDao(): FixturesDao
    abstract fun squadsDao(): SquadsDao
    abstract fun standingsDao(): StandingsDao


    companion object {
        const val DATABASE_NAME = "cache_database"
    }
}
