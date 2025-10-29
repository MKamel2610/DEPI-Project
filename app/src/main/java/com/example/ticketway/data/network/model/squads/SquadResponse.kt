package com.example.ticketway.data.network.model.squads
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SquadResponse(
    val response: List<SquadItem>
)