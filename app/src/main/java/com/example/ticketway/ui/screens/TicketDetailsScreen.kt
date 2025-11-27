package com.example.ticketway.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
// --- FIXED: Added specific imports for all used icons ---
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
// --------------------------------------------------------
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketway.data.model.BookingItem
import com.example.ticketway.ui.ui.theme.*
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// Helper data class for organizing details
private data class TicketDetail(
    val icon: ImageVector,
    val label: String,
    // --- FIXED: Renamed from 'getValue' to 'valueExtractor' to avoid operator conflict
    val valueExtractor: (BookingItem) -> String,
    val valueColor: (BookingItem) -> Color = { DarkText }
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailsScreen(
    booking: BookingItem,
    onBack: () -> Unit
) {
    // --- Data Definitions ---

    val matchDetails = listOf(
        TicketDetail(
            icon = Icons.Default.SportsSoccer,
            label = "Match",
            valueExtractor = { "${it.homeTeam} vs ${it.awayTeam}" }
        ),
        TicketDetail(
            icon = Icons.Default.LocationOn,
            label = "Venue",
            valueExtractor = { it.stadiumName }
        ),
        TicketDetail(
            icon = Icons.Default.AccessTime,
            label = "Date & Time",
            valueExtractor = { formatBookingDateTime(it.matchDate) }
        ),
        TicketDetail(
            icon = Icons.Default.EmojiEvents,
            label = "Competition",
            valueExtractor = { "Football League (N/A)" }
        )
    )

    val ticketDetails = listOf(
        TicketDetail(
            icon = Icons.Default.ConfirmationNumber,
            label = "Tickets Count",
            valueExtractor = { "${it.seatCount} Total Tickets" }
        ),
        TicketDetail(
            icon = Icons.Default.AttachMoney,
            label = "Total Paid",
            valueExtractor = { "EGP ${String.format("%.2f", it.totalPrice.toDouble())}" }
        ),
        TicketDetail(
            icon = Icons.Default.Numbers,
            label = "Seat Section",
            valueExtractor = { "Section TBA" }
        )
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ticket Details", color = DarkText) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = DarkText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // Booking Reference Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryGreen.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Booking Reference:",
                        fontSize = 14.sp,
                        color = DarkText.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        booking.bookingId.uppercase(Locale.ROOT),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Payment Status: ${booking.paymentStatus}",
                        fontSize = 12.sp,
                        color = if (booking.paymentStatus == "PENDING") MaterialTheme.colorScheme.error else PrimaryGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Match Details Card
            DetailsCard {
                matchDetails.forEachIndexed { index, detail ->
                    if (index > 0) {
                        Divider(color = LightGray, thickness = 1.dp)
                    }
                    DetailRow(
                        icon = detail.icon,
                        label = detail.label,
                        // --- FIXED: Using the new name 'valueExtractor'
                        value = detail.valueExtractor(booking),
                        valueColor = detail.valueColor(booking)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Ticket Breakdown Card
            DetailsCard {
                Text(
                    "Ticket Breakdown",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )
                Divider(color = LightGray, thickness = 1.dp)

                if (booking.regularCount > 0) {
                    DetailRow(
                        icon = Icons.Default.Circle,
                        label = "Regular Tier",
                        value = "${booking.regularCount} Tickets",
                        valueColor = DarkText
                    )
                }
                if (booking.premiumCount > 0) {
                    if (booking.regularCount > 0) Divider(color = LightGray, thickness = 1.dp)
                    DetailRow(
                        icon = Icons.Default.Star,
                        label = "Premium Tier",
                        value = "${booking.premiumCount} Tickets",
                        valueColor = DarkText
                    )
                }

                Divider(color = LightGray, thickness = 1.dp)
                ticketDetails.forEachIndexed { index, detail ->
                    DetailRow(
                        icon = detail.icon,
                        label = detail.label,
                        // --- FIXED: Using the new name 'valueExtractor'
                        value = detail.valueExtractor(booking),
                        valueColor = if (detail.label == "Total Paid") PrimaryGreen else detail.valueColor(booking)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Call to Action
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.QrCode2, contentDescription = "View QR Code", modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Ticket QR Code", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Helper Composable for consistent card styling
@Composable
fun DetailsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp), content = content)
    }
}

// Helper Composable for detail rows
@Composable
fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = DarkText
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, contentDescription = label, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
            Text(label, fontSize = 14.sp, color = DarkText.copy(alpha = 0.7f))
        }
        Text(
            value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = valueColor,
            textAlign = TextAlign.End
        )
    }
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
