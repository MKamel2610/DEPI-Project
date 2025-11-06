package com.example.ticketway.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = false)
    val bookingId: String,
    val fixtureId: Int,
    val userId: String,
    val matchTitle: String,
    val stadiumName: String,
    val matchDate: String,
    val seatTier: String,
    val seatCount: Int,
    val pricePerSeat: Double,
    val totalPrice: Double,
    val paymentStatus: String
)
