package com.example.ticketway.data.network.model.fixtures
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class Periods(
    val first: Long?,
    val second: Long?
)