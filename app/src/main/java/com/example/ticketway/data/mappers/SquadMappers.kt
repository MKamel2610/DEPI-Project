package com.example.ticketway.data.mappers

import com.example.ticketway.data.local.entities.SquadEntity
import com.example.ticketway.data.network.model.squads.SquadResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val adapter = moshi.adapter(SquadResponse::class.java)

fun SquadResponse.toEntity(teamId: Int): SquadEntity {
    return SquadEntity(
        teamId = teamId,
        dataJson = adapter.toJson(this)
    )
}

fun SquadEntity.toModel(): SquadResponse? {
    return adapter.fromJson(dataJson)
}
