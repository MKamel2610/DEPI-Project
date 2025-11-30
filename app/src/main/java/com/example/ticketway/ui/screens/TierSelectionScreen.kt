package com.example.ticketway.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ticketway.data.network.model.fixtures.FixtureItem
import com.example.ticketway.ui.booking.BookingViewModel
import com.example.ticketway.ui.user.UserViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.ticketway.ui.screens.tierselection.previews.TierSelectionContent as StatelessTierSelectionContent
import com.example.ticketway.ui.screens.tierselection.previews.TierSelectionUIData

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TierSelectionScreen(
    bookingViewModel: BookingViewModel,
    userViewModel: UserViewModel,
    onBookingConfirmed: () -> Unit,
    onBack: () -> Unit
) {
    // 1. Collect all necessary state
    val fixture by bookingViewModel.currentFixture.collectAsState()
    val userProfile by userViewModel.profile.collectAsState()
    val regCount by bookingViewModel.regularCount.collectAsState()
    val premCount by bookingViewModel.premiumCount.collectAsState()
    val totalTickets by bookingViewModel.totalTickets.collectAsState()
    val totalPrice by bookingViewModel.totalPrice.collectAsState()
    val errorMsg by bookingViewModel.errorMsg.collectAsState()
    val isLoading by bookingViewModel.isLoading.collectAsState()

    val maxTickets = 4

    if (fixture == null || userProfile == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    val formattedDateTime = formatDateTime(fixture!!.fixture.date)

    val uiData = mapToTierSelectionUIData(
        fixture = fixture!!,
        userName = userProfile!!.name,
        regCount = regCount,
        premCount = premCount,
        totalTickets = totalTickets,
        errorMsg = errorMsg,
        isLoading = isLoading,
        maxTickets = maxTickets
    )

    // 4. Action Handlers (Delegation)
    val onQuantityChange: (String, Int) -> Unit = { tier, count ->
        bookingViewModel.updateSelection(tier, count)
    }

    val onBackAction: () -> Unit = {
        bookingViewModel.clearBookingState()
        onBack()
    }

    // 5. Call Stateless Content
    StatelessTierSelectionContent(
        data = uiData,
        totalPrice = totalPrice,
        onBack = onBackAction,
        onNavigateToSummary = onBookingConfirmed,
        onQuantityChange = onQuantityChange
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun mapToTierSelectionUIData(
    fixture: FixtureItem,
    userName: String,
    regCount: Int,
    premCount: Int,
    totalTickets: Int,
    errorMsg: String?,
    isLoading: Boolean,
    maxTickets: Int
): TierSelectionUIData {
    val formattedDateTime = formatDateTime(fixture.fixture.date)
    return TierSelectionUIData(
        homeTeam = fixture.teams.home.name,
        awayTeam = fixture.teams.away.name,
        homeLogoUrl = fixture.teams.home.logo,
        awayLogoUrl = fixture.teams.away.logo,
        venueName = fixture.fixture.venue?.name ?: "Venue TBA",
        formattedDateTime = formattedDateTime,
        userName = userName,
        regCount = regCount,
        premCount = premCount,
        totalTickets = totalTickets,
        maxTickets = maxTickets,
        errorMsg = errorMsg,
        isLoading = isLoading
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateTime(dateTimeString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
        val shiftedDateTime = dateTime.plusDays(7)
        shiftedDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy • HH:mm"))
    } catch (e: Exception) {
        dateTimeString
    }
}
