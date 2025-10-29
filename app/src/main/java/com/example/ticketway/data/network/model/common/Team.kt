package com.example.ticketway.data.network.model.common

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Team(
    val id: Int,
    val name: String,
    val logo: String,
    val winner: Boolean?
)