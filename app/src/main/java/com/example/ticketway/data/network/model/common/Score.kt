package com.example.ticketway.data.network.model.common

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Score(
    val halftime: Goals?,
    val fulltime: Goals?,
    val extratime: Goals?,
    val penalty: Goals?
)