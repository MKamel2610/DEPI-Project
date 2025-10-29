package com.example.ticketway.data.network.model.standings
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StandingGoals(
    val `for`: Int,
    val against: Int
)