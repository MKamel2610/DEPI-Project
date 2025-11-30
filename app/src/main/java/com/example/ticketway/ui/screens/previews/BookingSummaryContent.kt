package com.example.ticketway.ui.screens.previews

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class PreviewFixture(
    val homeTeam: String,
    val awayTeam: String,
    val homeLogo: String?,
    val awayLogo: String?,
    val venue: String,
    val dateTime: String
)

private const val REGULAR_PRICE = 100
private const val PREMIUM_PRICE = 200
private const val VAT_RATE = 0.0

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingSummaryContent(
    fixture: PreviewFixture?,
    regCount: Int,
    premCount: Int,
    subtotal: Double,
    vatAmount: Double,
    total: Double,
    onBack: () -> Unit,
    onProceedToPayment: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Review Booking",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Go back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        bottomBar = {
            SummaryBottomBar(
                totalPrice = total,
                onProceedToPayment = onProceedToPayment
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (fixture == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(
                    "Error: No booking data found.",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
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

            MatchSummaryCard(fixture)

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

@Composable
private fun MatchSummaryCard(fixture: PreviewFixture) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "Match Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Divider(color = MaterialTheme.colorScheme.surfaceVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeamLogoDisplay(fixture.homeLogo, fixture.homeTeam)
                Text(
                    "vs",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TeamLogoDisplay(fixture.awayLogo, fixture.awayTeam)
            }

            Spacer(modifier = Modifier.height(8.dp))

            SummaryDetailRow(
                icon = Icons.Default.LocationOn,
                label = "Venue",
                value = fixture.venue
            )
            SummaryDetailRow(
                icon = Icons.Default.CalendarToday,
                label = "Date & Time",
                value = fixture.dateTime
            )
        }
    }
}

@Composable
private fun TeamLogoDisplay(logoUrl: String?, name: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = logoUrl,
            contentDescription = name,
            modifier = Modifier.size(36.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            name,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SummaryDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        Text(
            value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Ticket Summary",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
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
                Text(
                    "Total Payable",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
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
private fun PriceRow(
    label: String,
    pricePerUnit: String,
    totalValue: Double,
    isBold: Boolean = false
) {
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
    onProceedToPayment: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // FIX: Apply windowInsetsPadding for the navigation bar
                .windowInsetsPadding(WindowInsets.navigationBars)
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
                onClick = onProceedToPayment,
                modifier = Modifier
                    .width(180.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Proceed to Payment", // Fixed typo "Procede" -> "Proceed"
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        }
    }
}


// ==================== Previews ====================

@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Booking Summary - Regular Only", showBackground = true)
@Composable
private fun PreviewBookingSummaryRegularOnly() {
    val fixture = PreviewFixture(
        homeTeam = "Arsenal",
        awayTeam = "Chelsea",
        homeLogo = null,
        awayLogo = null,
        venue = "Emirates Stadium",
        dateTime = "Dec 15, 2024 • 19:30"
    )
    val regCount = 2
    val premCount = 0
    val subtotal = ((regCount * REGULAR_PRICE) + (premCount * PREMIUM_PRICE)).toDouble()
    val vatAmount = subtotal * VAT_RATE
    val total = subtotal + vatAmount

    MaterialTheme {
        BookingSummaryContent(
            fixture = fixture,
            regCount = regCount,
            premCount = premCount,
            subtotal = subtotal,
            vatAmount = vatAmount,
            total = total,
            onBack = {},
            onProceedToPayment = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Booking Summary - Premium Only", showBackground = true)
@Composable
private fun PreviewBookingSummaryPremiumOnly() {
    val fixture = PreviewFixture(
        homeTeam = "Manchester United",
        awayTeam = "Liverpool",
        homeLogo = null,
        awayLogo = null,
        venue = "Old Trafford",
        dateTime = "Dec 20, 2024 • 16:00"
    )
    val regCount = 0
    val premCount = 2
    val subtotal = ((regCount * REGULAR_PRICE) + (premCount * PREMIUM_PRICE)).toDouble()
    val vatAmount = subtotal * VAT_RATE
    val total = subtotal + vatAmount

    MaterialTheme {
        BookingSummaryContent(
            fixture = fixture,
            regCount = regCount,
            premCount = premCount,
            subtotal = subtotal,
            vatAmount = vatAmount,
            total = total,
            onBack = {},
            onProceedToPayment = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Booking Summary - Mixed Tickets", showBackground = true)
@Composable
private fun PreviewBookingSummaryMixed() {
    val fixture = PreviewFixture(
        homeTeam = "Real Madrid",
        awayTeam = "Barcelona",
        homeLogo = null,
        awayLogo = null,
        venue = "Santiago Bernabéu",
        dateTime = "Jan 05, 2025 • 21:00"
    )
    val regCount = 2
    val premCount = 2
    val subtotal = ((regCount * REGULAR_PRICE) + (premCount * PREMIUM_PRICE)).toDouble()
    val vatAmount = subtotal * VAT_RATE
    val total = subtotal + vatAmount

    MaterialTheme {
        BookingSummaryContent(
            fixture = fixture,
            regCount = regCount,
            premCount = premCount,
            subtotal = subtotal,
            vatAmount = vatAmount,
            total = total,
            onBack = {},
            onProceedToPayment = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Booking Summary - Max Tickets", showBackground = true)
@Composable
private fun PreviewBookingSummaryMaxTickets() {
    val fixture = PreviewFixture(
        homeTeam = "Bayern Munich",
        awayTeam = "Borussia Dortmund",
        homeLogo = null,
        awayLogo = null,
        venue = "Allianz Arena",
        dateTime = "Jan 12, 2025 • 18:30"
    )
    val regCount = 4
    val premCount = 0
    val subtotal = ((regCount * REGULAR_PRICE) + (premCount * PREMIUM_PRICE)).toDouble()
    val vatAmount = subtotal * VAT_RATE
    val total = subtotal + vatAmount

    MaterialTheme {
        BookingSummaryContent(
            fixture = fixture,
            regCount = regCount,
            premCount = premCount,
            subtotal = subtotal,
            vatAmount = vatAmount,
            total = total,
            onBack = {},
            onProceedToPayment = {}
        )
    }
}