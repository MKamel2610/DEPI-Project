package com.example.ticketway.data.network.model.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Score(
    @field:Json(name = "halftime") val halftime: Goals?,
    @field:Json(name = "fulltime") val fulltime: Goals?,
    @field:Json(name = "extratime") val extratime: Goals?,
    @field:Json(name = "penalty") val penalty: Goals?
)