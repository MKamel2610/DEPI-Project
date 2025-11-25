package com.example.ticketway.data.model
data class BookingItem(
    val bookingId: String = "",
    val fixtureId: Int = 0,
    val userId: String = "",
    val homeTeam: String = "",
    val awayTeam: String = "",
    val stadiumName: String = "",
    val matchDate: String = "",
    val regularCount: Int = 0,
    val premiumCount: Int = 0,
    val seatCount: Int = 0,
    val totalPrice: Int = 0,
    val paymentStatus: String = "PENDING"
)
