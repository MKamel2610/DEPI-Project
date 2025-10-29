package com.example.ticketway.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fixtures_cache")
data class FixtureEntity(
    @PrimaryKey val id: Int,
    val date: String,
    val leagueId: Int,
    val leagueName: String,
    val leagueLogo: String?,
    val homeTeamId: Int,
    val homeTeamName: String,
    val homeTeamLogo: String?,
    val awayTeamId: Int,
    val awayTeamName: String,
    val awayTeamLogo: String?,
    val goalsHome: Int?,
    val goalsAway: Int?,
    val matchStatus: String?,
    val matchTime: String?,
    val venue: String?,
    val referee: String?,
    val cachedAt: Long,
    val source: String
)
