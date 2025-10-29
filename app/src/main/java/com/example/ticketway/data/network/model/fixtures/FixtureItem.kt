package com.example.ticketway.data.network.model.fixtures
import com.example.ticketway.data.network.model.common.League
import com.example.ticketway.data.network.model.common.Goals
import com.example.ticketway.data.network.model.common.Score
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FixtureItem(
    val fixture: FixtureDetails,
    val league: League,
    val teams: Teams,
    val goals: Goals,
    val score: Score
)
