package com.example.ticketway.ui.screens.previews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ConfirmationNumber // Imported necessary icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.background

// Mock data class for preview (Public for MyTicketsScreen to map data)
data class PreviewBooking(
    val bookingId: String,
    val homeTeam: String,
    val awayTeam: String,
    val stadiumName: String,
    val matchDate: String,
    val seatCount: Int,
    val totalPrice: Int,
    val paymentStatus: String
    // NOTE: This PreviewBooking lacks regularCount and premiumCount needed for the detailed card.
    // Assuming for now seatCount = total, but in a real app, PreviewBooking should be updated.
)

@Composable
fun MyTicketsContent(
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
                                DetailedTicketCard(booking) // Use the new detailed card
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
                            Icon( // Replaced emoji
                                Icons.Default.ConfirmationNumber,
                                contentDescription = "No Tickets Icon",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
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

// Replicating the detailed structure from MyTicketCard.kt, adapted for PreviewBooking

@Composable
private fun DetailedTicketCard(booking: PreviewBooking) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 1. Header Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "${booking.homeTeam} vs ${booking.awayTeam}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // 2. Details & Price
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

                // Status Indicator
                Text(
                    text = "Status: ${booking.paymentStatus}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (booking.paymentStatus == "PAID") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )

                Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

                // Details Rows
                CardDetailRow(
                    icon = Icons.Default.LocationOn,
                    label = "Stadium:",
                    value = booking.stadiumName
                )
                CardDetailRow(
                    icon = Icons.Default.CalendarToday,
                    label = "Time:",
                    value = booking.matchDate
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tickets Summary & Price
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Tickets:", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
                        CardTicketDetailRow(label = "Total Seats:", value = "${booking.seatCount}")

                        // NOTE: Since PreviewBooking lacks regularCount/premiumCount, we'll omit the conditional rows
                        // or show a placeholder if we cannot change the PreviewBooking structure.
                        // For accurate UI representation, the next two blocks are commented out:
                        /*
                        if (booking.regularCount > 0) {
                            CardTicketDetailRow(label = "- Regular:", value = "${booking.regularCount}")
                        }
                        if (booking.premiumCount > 0) {
                            CardTicketDetailRow(label = "- Premium:", value = "${booking.premiumCount}")
                        }
                        */
                    }

                    // Total Price Box
                    Surface(
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                "Paid:",
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                            Text(
                                "EGP ${booking.totalPrice}.00",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CardDetailRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        Text(value, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun CardTicketDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(start = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
            Icon( // Replaced emoji
                Icons.Default.ConfirmationNumber,
                contentDescription = "Ticket Icon",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

// ==================== Previews (Use DetailedTicketCard) ====================

// Previews remain the same, they now call MyTicketsContent which calls DetailedTicketCard
// ... (Previews are identical to the original structure, calling MyTicketsContent)

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