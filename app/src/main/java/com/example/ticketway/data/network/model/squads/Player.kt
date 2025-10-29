package com.example.ticketway.data.network.model.squads
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Player(
    val id: Int,
    val name: String,
    val age: Int?,
    val number: Int?,
    val position: String,
    val photo: String
)