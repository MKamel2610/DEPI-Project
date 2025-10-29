package com.example.ticketway.data.network.model.standings

import com.example.ticketway.data.network.model.common.Team
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamStanding(
    val rank: Int,
    val team: Team,
    val points: Int,
    val goalsDiff: Int,
    val group: String?,
    val form: String?,
    val status: String?,
    val description: String?,
    val all: StandingStats,
    val home: StandingStats,
    val away: StandingStats,
    val update: String
)