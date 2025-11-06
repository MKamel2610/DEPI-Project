package com.example.ticketway.data.mappers

import com.example.ticketway.data.local.entities.BookingEntity
import com.example.ticketway.data.network.model.booking.BookingItem

fun BookingItem.toEntity(): BookingEntity {
    return BookingEntity(
        bookingId = bookingId,
        fixtureId = fixtureId,
        userId = userId,
        matchTitle = matchTitle,
        stadiumName = stadiumName,
        matchDate = matchDate,
        seatTier = seatTier,
        seatCount = seatCount,
        pricePerSeat = pricePerSeat,
        totalPrice = totalPrice,
        paymentStatus = paymentStatus
    )
}

fun BookingEntity.toModel(): BookingItem {
    return BookingItem(
        bookingId = bookingId,
        fixtureId = fixtureId,
        userId = userId,
        matchTitle = matchTitle,
        stadiumName = stadiumName,
        matchDate = matchDate,
        seatTier = seatTier,
        seatCount = seatCount,
        pricePerSeat = pricePerSeat,
        totalPrice = totalPrice,
        paymentStatus = paymentStatus
    )
}
