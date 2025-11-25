package com.example.ticketway.ui.screens.booking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CalendarToday // NEW Import
import androidx.compose.material.icons.filled.LocationOn // NEW Import
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ticketway.data.network.model.fixtures.FixtureItem
import com.example.ticketway.ui.booking.BookingViewModel
import com.example.ticketway.ui.components.tierselectionscreen.TierOptionCard
import com.example.ticketway.ui.components.tierselectionscreen.BookingBottomBar
import com.example.ticketway.ui.ui.theme.*
import com.example.ticketway.ui.user.UserViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneOffset


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TierSelectionScreen(
    bookingViewModel: BookingViewModel,
    userViewModel: UserViewModel,
    onBookingConfirmed: () -> Unit,
    onBack: () -> Unit
) {
    val fixture by bookingViewModel.currentFixture.collectAsState()
    val userProfile by userViewModel.profile.collectAsState()

    val regCount by bookingViewModel.regularCount.collectAsState()
    val premCount by bookingViewModel.premiumCount.collectAsState()
    val totalTickets by bookingViewModel.totalTickets.collectAsState()
    val errorMsg by bookingViewModel.errorMsg.collectAsState()
    val isLoading by bookingViewModel.isLoading.collectAsState()

    val maxTickets = 4

    // Ensure we have data
    if (fixture == null || userProfile == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Tickets", color = DarkText, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        bookingViewModel.clearBookingState()
                        onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightGray)
            )
        },
        bottomBar = {
            BookingBottomBar(
                bookingViewModel = bookingViewModel,
                onNavigateToSummary = onBookingConfirmed
            )
        },
        modifier = Modifier.fillMaxSize().background(LightGray)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(LightGray)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 1. Match Info Card
            MatchInfoCard(fixture!!)

            Spacer(modifier = Modifier.height(16.dp))

            // 2. User Info/Limit Banner
            LimitBanner(userProfile!!.name, totalTickets, maxTickets, errorMsg)

            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    color = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Tier Selection
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Select Seating Tier",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Regular Tier
                TierOptionCard(
                    title = "Regular",
                    price = 100,
                    quantity = regCount,
                    onQuantityChange = { count ->
                        bookingViewModel.updateSelection("Regular", count)
                    },
                    canIncrease = totalTickets < maxTickets
                )

                // Premium Tier
                TierOptionCard(
                    title = "Premium",
                    price = 200,
                    quantity = premCount,
                    onQuantityChange = { count ->
                        bookingViewModel.updateSelection("Premium", count)
                    },
                    canIncrease = totalTickets < maxTickets
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// --- Helper Composables ---

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MatchInfoCard(fixture: FixtureItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Teams and Logos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeamDisplay(fixture.teams.home.name, fixture.teams.home.logo, Alignment.Start)
                Text("vs", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = LightText)
                TeamDisplay(fixture.teams.away.name, fixture.teams.away.logo, Alignment.End)
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Venue
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.LocationOn, // FIXED: Using LocationOn Icon
                    contentDescription = "Venue",
                    tint = DarkText,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = fixture.fixture.venue?.name ?: "Venue TBA",
                    fontSize = 15.sp,
                    color = DarkText,
                    fontWeight = FontWeight.Medium
                )
            }

            // Date and Time Display
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.CalendarToday, // FIXED: Using CalendarToday Icon
                    contentDescription = "Date",
                    tint = DarkText,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = formatDateTime(fixture.fixture.date),
                    fontSize = 15.sp,
                    color = DarkText,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun TeamDisplay(name: String, logoUrl: String?, alignment: Alignment.Horizontal) {
    Column(horizontalAlignment = alignment) {
        AsyncImage(
            model = logoUrl,
            contentDescription = name,
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
    }
}

@Composable
private fun LimitBanner(userName: String, totalTickets: Int, maxTickets: Int, errorMsg: String?) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = PrimaryGreen.copy(alpha = 0.1f)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // User Greeting
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = "User", tint = PrimaryGreen, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Welcome, $userName!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Ticket Limit
            val remaining = maxTickets - totalTickets
            Text(
                text = "Selected: ${totalTickets} / ${maxTickets} tickets. (${remaining} remaining)",
                fontSize = 14.sp,
                color = DarkText
            )

            // Error Message
            if (errorMsg != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = errorMsg,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
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