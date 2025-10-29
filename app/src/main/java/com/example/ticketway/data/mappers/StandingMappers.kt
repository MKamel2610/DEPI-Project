package com.example.ticketway.data.mappers

import com.example.ticketway.data.local.entities.StandingEntity
import com.example.ticketway.data.network.model.standings.StandingsResponse
import com.squareup.moshi.Moshi

private val moshi = Moshi.Builder().build()
private val adapter = moshi.adapter(StandingsResponse::class.java)

fun StandingsResponse.toEntity(leagueId: Int, season: Int): StandingEntity {
    val json = adapter.toJson(this)
    return StandingEntity(
        leagueId = leagueId,
        season = season,
        dataJson = json
    )
}

fun StandingEntity.toModel(): StandingsResponse? {
    return adapter.fromJson(dataJson)
}
