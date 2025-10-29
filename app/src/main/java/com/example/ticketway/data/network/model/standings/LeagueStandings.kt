package com.example.ticketway.data.network.model.standings
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LeagueStandings(
    val league: LeagueTable
)