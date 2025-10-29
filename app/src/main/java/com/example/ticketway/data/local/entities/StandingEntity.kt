package com.example.ticketway.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "standings")
data class StandingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val leagueId: Int,
    val season: Int,
    val dataJson: String,   // store the full JSON of the standings
    val cachedAt: Long = System.currentTimeMillis()
)
