package com.example.ticketway.data.network.model.standings
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LeagueTable(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String?,
    val season: Int,
    val standings: List<List<TeamStanding>>
)