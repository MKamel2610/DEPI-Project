package com.example.ticketway.data.network.model.booking

data class BookingItem(
    val bookingId: String = "",
    val fixtureId: Int,
    val userId: String,
    val matchTitle: String,
    val stadiumName: String,
    val matchDate: String,
    val seatTier: String,
    val seatCount: Int,
    val pricePerSeat: Double,
    val totalPrice: Double,
    val paymentStatus: String = "PENDING" // or "PAID"
)
