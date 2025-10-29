package com.example.ticketway.data.network.model.common

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Goals(
    val home: Int?,
    val away: Int?
)