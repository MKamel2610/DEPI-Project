package com.example.ticketway.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.ticketway.data.network.model.fixtures.FixtureItem
import com.example.ticketway.ui.booking.BookingViewModel
// Import the stateless content and its data class
import com.example.ticketway.ui.screens.previews.BookingSummaryContent as StatelessBookingSummaryContent
import com.example.ticketway.ui.screens.previews.PreviewFixture
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val REGULAR_PRICE = 100
private const val PREMIUM_PRICE = 200
private const val VAT_RATE = 0.0

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSummaryScreen(
    bookingViewModel: BookingViewModel,
    onProcedeToPayment: () -> Unit,
    onBack: () -> Unit
) {
    // Collect state from ViewModel
    val fixture by bookingViewModel.currentFixture.collectAsState()
    val regCount by bookingViewModel.regularCount.collectAsState()
    val premCount by bookingViewModel.premiumCount.collectAsState()

    // Calculate derived state
    val subtotal = ((regCount * REGULAR_PRICE) + (premCount * PREMIUM_PRICE)).toDouble()
    val vatAmount = subtotal * VAT_RATE
    val total = subtotal + vatAmount

    // Map FixtureItem (data model) to PreviewFixture (UI model) for the stateless component
    val summaryFixture: PreviewFixture? = fixture?.let {
        mapFixtureItemToPreviewFixture(it)
    }

    // Call Stateless Composable
    StatelessBookingSummaryContent(
        fixture = summaryFixture,
        regCount = regCount,
        premCount = premCount,
        subtotal = subtotal,
        vatAmount = vatAmount,
        total = total,
        onBack = onBack,
        onProceedToPayment = onProcedeToPayment
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun mapFixtureItemToPreviewFixture(fixtureItem: FixtureItem): PreviewFixture {
    val dateTimeString = formatDateTime(fixtureItem.fixture.date)
    return PreviewFixture(
        homeTeam = fixtureItem.teams.home.name,
        awayTeam = fixtureItem.teams.away.name,
        homeLogo = fixtureItem.teams.home.logo,
        awayLogo = fixtureItem.teams.away.logo,
        venue = fixtureItem.fixture.venue?.name ?: "Venue TBA",
        dateTime = dateTimeString
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateTime(dateTimeString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
        val shiftedDateTime = dateTime.plusDays(7) // Backend returns date 7 days behind
        shiftedDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy • HH:mm"))
    } catch (e: Exception) {
        dateTimeString
    }
}