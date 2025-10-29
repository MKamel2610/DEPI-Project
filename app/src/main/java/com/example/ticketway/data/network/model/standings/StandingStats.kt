package com.example.ticketway.data.network.model.standings

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StandingStats(
    val played: Int,
    val win: Int,
    val draw: Int,
    val lose: Int,
    val goals: StandingGoals
)