package com.example.ticketway.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ticketway.data.model.BookingItem
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
// Import the new stateless content and its data class
import com.example.ticketway.ui.screens.ticketdetails.previews.TicketDetailsContent as StatelessTicketDetailsContent
import com.example.ticketway.ui.screens.ticketdetails.previews.TicketUIData

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TicketDetailsScreen(
    booking: BookingItem,
    onBack: () -> Unit
) {
    // 1. Data Processing and Mapping
    val formattedDateTime = formatBookingDateTime(booking.matchDate)

    val uiData = mapBookingItemToTicketUIData(booking, formattedDateTime)

    // 2. Action Handlers (Delegation)
    val onViewQrCode: () -> Unit = {
        // Implement navigation or logic for viewing the QR code here
        // Currently, it's a placeholder function, but it's now hoisted.
    }

    // 3. Call Stateless Content
    StatelessTicketDetailsContent(
        data = uiData,
        onBack = onBack,
        onViewQrCode = onViewQrCode
    )
}

// Helper function to map the domain model to the UI model
private fun mapBookingItemToTicketUIData(booking: BookingItem, formattedDateTime: String): TicketUIData {
    return TicketUIData(
        bookingId = booking.bookingId,
        homeTeam = booking.homeTeam,
        awayTeam = booking.awayTeam,
        stadiumName = booking.stadiumName,
        formattedDateTime = formattedDateTime,
        regularCount = booking.regularCount,
        premiumCount = booking.premiumCount,
        seatCount = booking.seatCount,
        totalPrice = booking.totalPrice,
        paymentStatus = booking.paymentStatus
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatBookingDateTime(dateTimeString: String): String {
    return try {
        val dateTimeUtc = ZonedDateTime.parse(
            dateTimeString,
            DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("UTC"))
        ).plusDays(7)

        val localDateTime = dateTimeUtc.withZoneSameInstant(ZoneId.systemDefault())

        localDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy • HH:mm"))
    } catch (e: Exception) {
        dateTimeString
    }
}

// Removed all UI implementation details and helper composables (DetailsCard, DetailRow, etc.)
// They are now in TicketDetailsContent.kt.