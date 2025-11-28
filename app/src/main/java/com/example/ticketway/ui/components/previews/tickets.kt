package com.example.ticketway.ui.components.previews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ticketway.data.model.BookingItem
import com.example.ticketway.ui.components.MyTicketCard
import com.example.ticketway.ui.theme.TicketWayTheme


val mockBookingPaid = BookingItem(
    homeTeam = "Real Madrid",
    awayTeam = "FC Barcelona",
    stadiumName = "Santiago Bernabéu",
    matchDate = "Nov 27, 2025 • 22:00",
    seatCount = 3,
    regularCount = 2,
    premiumCount = 1,
    totalPrice = 4500,
    paymentStatus = "PAID"
)

val mockBookingPending = mockBookingPaid.copy(
    homeTeam = "Liverpool",
    awayTeam = "Man City",
    matchDate = "Dec 05, 2025 • 17:30",
    seatCount = 1,
    regularCount = 1,
    premiumCount = 0,
    totalPrice = 1200,
    paymentStatus = "PENDING"
)


@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Paid Ticket")
@Composable
fun PreviewMyTicketCardPaid() {
    TicketWayTheme {
        MyTicketCard(booking = mockBookingPaid)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Pending Ticket")
@Composable
fun PreviewMyTicketCardPending() {
    TicketWayTheme {
        MyTicketCard(booking = mockBookingPending)
    }
}