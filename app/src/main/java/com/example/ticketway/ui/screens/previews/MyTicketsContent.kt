package com.example.ticketway.ui.screens.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Mock data class for preview
private data class PreviewBooking(
    val bookingId: String,
    val homeTeam: String,
    val awayTeam: String,
    val stadiumName: String,
    val matchDate: String,
    val seatCount: Int,
    val totalPrice: Int,
    val paymentStatus: String
)

@Composable
private fun MyTicketsContent(
    tickets: List<PreviewBooking>,
    isLoading: Boolean,
    errorMessage: String?,
    onTicketClick: (PreviewBooking) -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header
            TicketsHeader(ticketCount = tickets.size)

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                tickets.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 4.dp, bottom = 60.dp)
                    ) {
                        items(tickets) { booking ->
                            Box(modifier = Modifier.clickable { onTicketClick(booking) }) {
                                TicketCard(booking)
                            }
                        }
                    }
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🎟️", fontSize = 64.sp)
                            Text(
                                text = "No Tickets Found",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = errorMessage ?: "Book a match to see your tickets here once payment is successful.",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            if (errorMessage != null) {
                                Button(
                                    onClick = onRetry,
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    Text("Retry Loading")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TicketsHeader(ticketCount: Int) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Your Bookings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$ticketCount active ticket${if (ticketCount != 1) "s" else ""}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
            Text(
                text = "🎫",
                fontSize = 40.sp,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun TicketCard(booking: PreviewBooking) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Teams
            Text(
                text = "${booking.homeTeam} vs ${booking.awayTeam}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Stadium
            Text(
                text = booking.stadiumName,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Date
            Text(
                text = booking.matchDate,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = MaterialTheme.colorScheme.surfaceVariant)

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${booking.seatCount} Tickets",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "EGP ${booking.totalPrice}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (booking.paymentStatus == "PAID")
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    else
                        MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = booking.paymentStatus,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (booking.paymentStatus == "PAID")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

// ==================== Previews ====================

@Preview(name = "My Tickets - Empty State", showBackground = true)
@Composable
private fun PreviewMyTicketsEmpty() {
    MaterialTheme {
        MyTicketsContent(
            tickets = emptyList(),
            isLoading = false,
            errorMessage = null,
            onTicketClick = {},
            onRetry = {}
        )
    }
}

@Preview(name = "My Tickets - Loading", showBackground = true)
@Composable
private fun PreviewMyTicketsLoading() {
    MaterialTheme {
        MyTicketsContent(
            tickets = emptyList(),
            isLoading = true,
            errorMessage = null,
            onTicketClick = {},
            onRetry = {}
        )
    }
}

@Preview(name = "My Tickets - Error State", showBackground = true)
@Composable
private fun PreviewMyTicketsError() {
    MaterialTheme {
        MyTicketsContent(
            tickets = emptyList(),
            isLoading = false,
            errorMessage = "Failed to load tickets. Please try again.",
            onTicketClick = {},
            onRetry = {}
        )
    }
}

@Preview(name = "My Tickets - Single Ticket", showBackground = true)
@Composable
private fun PreviewMyTicketsSingle() {
    MaterialTheme {
        MyTicketsContent(
            tickets = listOf(
                PreviewBooking(
                    bookingId = "BOOK123",
                    homeTeam = "Arsenal",
                    awayTeam = "Chelsea",
                    stadiumName = "Emirates Stadium",
                    matchDate = "Dec 15, 2024 • 19:30",
                    seatCount = 2,
                    totalPrice = 200,
                    paymentStatus = "PAID"
                )
            ),
            isLoading = false,
            errorMessage = null,
            onTicketClick = {},
            onRetry = {}
        )
    }
}

@Preview(name = "My Tickets - Multiple Tickets", showBackground = true)
@Composable
private fun PreviewMyTicketsMultiple() {
    MaterialTheme {
        MyTicketsContent(
            tickets = listOf(
                PreviewBooking(
                    bookingId = "BOOK123",
                    homeTeam = "Arsenal",
                    awayTeam = "Chelsea",
                    stadiumName = "Emirates Stadium",
                    matchDate = "Dec 15, 2024 • 19:30",
                    seatCount = 2,
                    totalPrice = 200,
                    paymentStatus = "PAID"
                ),
                PreviewBooking(
                    bookingId = "BOOK456",
                    homeTeam = "Manchester United",
                    awayTeam = "Liverpool",
                    stadiumName = "Old Trafford",
                    matchDate = "Dec 20, 2024 • 16:00",
                    seatCount = 4,
                    totalPrice = 600,
                    paymentStatus = "PAID"
                ),
                PreviewBooking(
                    bookingId = "BOOK789",
                    homeTeam = "Real Madrid",
                    awayTeam = "Barcelona",
                    stadiumName = "Santiago Bernabéu",
                    matchDate = "Jan 05, 2025 • 21:00",
                    seatCount = 3,
                    totalPrice = 500,
                    paymentStatus = "PENDING"
                )
            ),
            isLoading = false,
            errorMessage = null,
            onTicketClick = {},
            onRetry = {}
        )
    }
}