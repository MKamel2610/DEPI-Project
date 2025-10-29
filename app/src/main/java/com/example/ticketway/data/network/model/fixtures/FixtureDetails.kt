package com.example.ticketway.data.network.model.fixtures
import com.example.ticketway.data.network.model.common.Venue
import com.example.ticketway.data.network.model.common.Status
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FixtureDetails(
    val id: Int,
    val referee: String?,
    val timezone: String,
    val date: String,
    val timestamp: Long,
    val periods: Periods?,
    val venue: Venue?,
    val status: Status
)