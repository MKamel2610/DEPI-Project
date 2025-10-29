package com.example.ticketway.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ticketway.data.local.entities.FixtureEntity

@Dao
interface FixturesDao {

    @Query("SELECT * FROM fixtures_cache WHERE date = :date ORDER BY leagueId, id")
    suspend fun getFixturesByDate(date: String): List<FixtureEntity>

    @Query("SELECT * FROM fixtures_cache WHERE leagueId = :leagueId ORDER BY date")
    suspend fun getFixturesByLeague(leagueId: Int): List<FixtureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixtures(fixtures: List<FixtureEntity>)

    @Query("DELETE FROM fixtures_cache WHERE date = :date")
    suspend fun clearFixturesByDate(date: String)
}
