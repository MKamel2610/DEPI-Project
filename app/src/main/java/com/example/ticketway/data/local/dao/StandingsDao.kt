package com.example.ticketway.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ticketway.data.local.entities.StandingEntity

@Dao
interface StandingsDao {

    @Query("SELECT * FROM standings WHERE leagueId = :leagueId AND season = :season")
    suspend fun getStandings(leagueId: Int, season: Int): List<StandingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStandings(standings: List<StandingEntity>)

    @Query("DELETE FROM standings")
    suspend fun clearAll()
}
