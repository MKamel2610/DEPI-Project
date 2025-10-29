package com.example.ticketway.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ticketway.data.local.entities.SquadEntity

@Dao
interface SquadsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSquad(squad: SquadEntity)

    @Query("SELECT * FROM squads WHERE teamId = :teamId")
    suspend fun getSquadByTeamId(teamId: Int): SquadEntity?

}
