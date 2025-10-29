package com.example.ticketway.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "squads")
data class SquadEntity(
    @PrimaryKey val teamId: Int,
    val dataJson: String
)
