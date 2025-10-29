package com.example.ticketway.data.network.model.common

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Status(
    val long: String?,
    val short: String?,
    val elapsed: Int?,
    val extra: String?
)
