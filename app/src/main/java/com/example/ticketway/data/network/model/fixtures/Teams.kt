package com.example.ticketway.data.network.model.fixtures
import com.example.ticketway.data.network.model.common.Team
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Teams(
    val home: Team,
    val away: Team
)
