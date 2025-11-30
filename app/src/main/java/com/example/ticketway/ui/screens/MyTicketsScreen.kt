package com.example.ticketway.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ticketway.data.model.BookingItem
import com.example.ticketway.ui.viewmodel.MyTicketsViewModel
// Import the stateless content and its data class
import com.example.ticketway.ui.screens.previews.MyTicketsContent as StatelessMyTicketsContent
import com.example.ticketway.ui.screens.previews.PreviewBooking

@Composable
fun MyTicketsScreen(
    viewModel: MyTicketsViewModel,
    onTicketClick: (BookingItem) -> Unit
) {
    // 1. Collect state from ViewModel (State Holder Logic)
    val tickets by viewModel.tickets.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // 2. Define event handlers
    val onRetry = { viewModel.loadTickets() }

    LaunchedEffect(Unit) {
        viewModel.loadTickets()
    }

    // 3. Map domain model (BookingItem) to UI model (PreviewBooking)
    val uiTickets = tickets.map { bookingItem ->
        mapBookingItemToPreviewBooking(bookingItem)
    }

    // 4. Define click handler to look up the original domain model using the ID
    val onUiTicketClick: (PreviewBooking) -> Unit = { previewBooking ->
        tickets.find { it.bookingId == previewBooking.bookingId }?.let { originalBooking ->
            onTicketClick(originalBooking)
        }
    }

    // 5. Call Stateless Composable
    StatelessMyTicketsContent(
        tickets = uiTickets,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onTicketClick = onUiTicketClick,
        onRetry = onRetry
    )
}

// Helper function to map the domain model to the UI model using the new flat BookingItem structure
private fun mapBookingItemToPreviewBooking(bookingItem: BookingItem): PreviewBooking {
    return PreviewBooking(
        bookingId = bookingItem.bookingId, // Accesses flat property
        homeTeam = bookingItem.homeTeam, // Accesses flat property
        awayTeam = bookingItem.awayTeam, // Accesses flat property
        stadiumName = bookingItem.stadiumName, // Accesses flat property
        matchDate = bookingItem.matchDate,
        seatCount = bookingItem.seatCount, // Accesses flat property
        totalPrice = bookingItem.totalPrice, // Accesses flat property
        paymentStatus = bookingItem.paymentStatus
    )
}

// NOTE: The separate formatDateTime utility function is no longer needed here
// as the matchDate is assumed to be pre-formatted in the simplified model.