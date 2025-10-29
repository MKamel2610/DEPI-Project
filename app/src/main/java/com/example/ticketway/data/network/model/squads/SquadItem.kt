package com.example.ticketway.data.network.model.squads
import com.example.ticketway.data.network.model.common.Team
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SquadItem(
    val team: Team,
    val players: List<Player>
)