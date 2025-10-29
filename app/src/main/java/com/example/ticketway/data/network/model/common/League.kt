package com.example.ticketway.data.network.model.common

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class League(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String?,
    val season: Int,
    val round: String?
)
