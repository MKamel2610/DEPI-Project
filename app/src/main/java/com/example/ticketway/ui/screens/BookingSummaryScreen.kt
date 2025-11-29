package com.example.ticketway.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ticketway.data.network.model.fixtures.FixtureItem
import com.example.ticketway.ui.booking.BookingViewModel
import com.example.ticketway.ui.components.tierselectionscreen.BookingBottomBar
import com.example.ticketway.ui.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val REGULAR_PRICE = 100
private const val PREMIUM_PRICE = 200
private const val VAT_RATE = 0.14

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSummaryScreen(
    bookingViewModel: BookingViewModel,
    onProcedeToPayment: () -> Unit,
    onBack: () -> Unit
) {
    val fixture by bookingViewModel.currentFixture.collectAsState()
    val regCount by bookingViewModel.regularCount.collectAsState()
    val premCount by bookingViewModel.premiumCount.collectAsState()

    val subtotal = ((regCount * REGULAR_PRICE) + (premCount * PREMIUM_PRICE)).toDouble()
    val vatAmount = subtotal * VAT_RATE
    val total = subtotal + vatAmount

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Booking", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )
        },
        bottomBar = {
            SummaryBottomBar(
                totalPrice = total,
                onProcedeToPayment = onProcedeToPayment
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        if (fixture == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Error: No booking data found.", color = MaterialTheme.colorScheme.error)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            MatchSummaryCard(fixture!!)
            Spacer(modifier = Modifier.height(16.dp))
            TicketDetailsCard(
                regCount = regCount,
                premCount = premCount,
                subtotal = subtotal,
                vatAmount = vatAmount,
                total = total
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MatchSummaryCard(fixture: FixtureItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Match Details", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Divider(color = MaterialTheme.colorScheme.surfaceVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeamLogoDisplay(fixture.teams.home.logo, fixture.teams.home.name)
                Text("vs", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                TeamLogoDisplay(fixture.teams.away.logo, fixture.teams.away.name)
            }

            Spacer(modifier = Modifier.height(8.dp))

            SummaryDetailRow(
                icon = Icons.Default.LocationOn,
                label = "Venue",
                value = fixture.fixture.venue?.name ?: "Venue TBA"
            )
            SummaryDetailRow(
                icon = Icons.Default.CalendarToday,
                label = "Date & Time",
                value = formatDateTime(fixture.fixture.date)
            )
        }
    }
}

@Composable
private fun TeamLogoDisplay(logoUrl: String?, name: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AsyncImage(
            model = logoUrl,
            contentDescription = name,
            modifier = Modifier.size(36.dp),
            contentScale = ContentScale.Fit
        )
        Text(name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun SummaryDetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
            Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun TicketDetailsCard(
    regCount: Int,
    premCount: Int,
    subtotal: Double,
    vatAmount: Double,
    total: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Ticket Summary", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Divider(color = MaterialTheme.colorScheme.surfaceVariant)

            if (regCount > 0) {
                PriceRow("Regular Ticket x$regCount", "EGP 100", regCount * REGULAR_PRICE.toDouble())
            }
            if (premCount > 0) {
                PriceRow("Premium Ticket x$premCount", "EGP 200", premCount * PREMIUM_PRICE.toDouble())
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))

            PriceRow("Subtotal", "", subtotal, isBold = true)
            PriceRow("VAT (${(VAT_RATE * 100).toInt()}%)", "", vatAmount)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Payable", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(
                    "EGP ${"%.2f".format(total)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun PriceRow(label: String, pricePerUnit: String, totalValue: Double, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                label,
                fontSize = 14.sp,
                fontWeight = if (isBold) FontWeight.SemiBold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (pricePerUnit.isNotEmpty()) {
                Text(
                    pricePerUnit,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            "EGP ${"%.2f".format(totalValue)}",
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.SemiBold else FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SummaryBottomBar(
    totalPrice: Double,
    onProcedeToPayment: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Total Payable",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "EGP ${"%.2f".format(totalPrice)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Button(
                onClick = onProcedeToPayment,
                modifier = Modifier
                    .width(180.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Proceed to Payment",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        }
    }
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
